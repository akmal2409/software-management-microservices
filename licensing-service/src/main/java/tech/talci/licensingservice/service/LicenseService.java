package tech.talci.licensingservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.talci.licensingservice.config.ServiceConfig;
import tech.talci.licensingservice.controller.LicenseController;
import tech.talci.licensingservice.model.License;
import tech.talci.licensingservice.model.Organization;
import tech.talci.licensingservice.repository.LicenseRepository;
import tech.talci.licensingservice.service.clients.OrganizationDiscoveryClient;
import tech.talci.licensingservice.service.clients.OrganizationFeignClient;
import tech.talci.licensingservice.service.clients.OrganizationRestTemplateClient;
import tech.talci.licensingservice.utils.UserContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
@Slf4j
public class LicenseService {

    private final LicenseRepository licenseRepository;
    private final ServiceConfig serviceConfig;
    private final OrganizationFeignClient feignClient;
    private final OrganizationDiscoveryClient discoveryClient;
    private final OrganizationRestTemplateClient restTemplateClient;

    public License getLicense(String licenseId, String organizationId, String clientType) {
        License license = licenseRepository
                .findByOrganizationIdAndLicenseId(organizationId, licenseId)
                .orElseThrow(() -> new IllegalArgumentException("License with " +
                        licenseId + " id was not found for a given organization ID: "
                        + organizationId));

        Organization organization = getOrganizationInfo(organizationId, clientType);

        if (organization != null) {
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
            license.setContactName(organization.getContactName());
            license.setOrganizationName(organization.getName());
        }

        license.add(
                linkTo(methodOn(LicenseController.class).getLicense(organizationId, license.getLicenseId(), clientType))
                        .withSelfRel(),
                linkTo(methodOn(LicenseController.class).createLicense(license))
                        .withRel("createLicense"),
                linkTo(methodOn(LicenseController.class).getLicenses(organizationId))
                        .withRel("getByOrganizationId"),
                linkTo(methodOn(LicenseController.class).updateLicense(license))
                        .withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class).deleteLicense(license.getLicenseId()))
                        .withRel("deleteLicense")
        );

        return license.withComment(serviceConfig.getExampleProperty());
    }

    //    @HystrixCommand(commandProperties = {
//            @HystrixProperty(
//                    name = "execution.isolation.thread.timeoutInMilliseconds",
//                    value = "12000"
//            )
//    })
    @HystrixCommand(fallbackMethod = "buildFallbackLicenseList",
                threadPoolKey = "getLicencesServiceThreadPool",
                commandKey = "getLicensesCommand")
    public List<License> getLicensesByOrganizationId(String organizationId) {
        log.debug("getLicenseByOrganization Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        randomlyRunLong();
        return licenseRepository.findByOrganizationId(organizationId);
    }

    private List<License> buildFallbackLicenseList(String organizationId) {
        License license = License.builder()
                .licenseId("000000-000-000")
                .organizationId(organizationId)
                .productName("Sorry no licensing information currently available")
                .build();

        return Collections.singletonList(license);
    }

    private void randomlyRunLong() {
        Random rand = new Random();
        int randNum = rand.nextInt((3 - 1) + 1) + 1;
        if (randNum == 3) {
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    private Organization getOrganizationInfo(String organizationId, String clientType) {
        Organization organization = null;

        switch (clientType) {
            case "feign":
                log.info("I am using feign client");
                organization = feignClient.getOrganization(organizationId);
                break;
            case "rest":
                log.info("I am using rest client");
                organization = restTemplateClient.getOrganization(organizationId);
                break;
            case "discovery":
                log.info("I am using discovery client");
                organization = discoveryClient.getOrganization(organizationId);
            default:
                organization = feignClient.getOrganization(organizationId);
        }

        return organization;
    }

    public License createLicense(License license) {
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);

        license.add(
                linkTo(methodOn(LicenseController.class).createLicense(license)).withSelfRel(),
                linkTo(methodOn(LicenseController.class)
                        .getLicense(license.getOrganizationId(), license.getLicenseId(), "feign"))
                        .withRel("getLicense"),
                linkTo(methodOn(LicenseController.class).getLicenses(license.getOrganizationId()))
                        .withRel("getByOrganizationId"),
                linkTo(methodOn(LicenseController.class).updateLicense(license)).withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class).deleteLicense(
                        license.getLicenseId())).withRel("deleteLicense")
        );

        return license.withComment(serviceConfig.getExampleProperty());
    }

    public License updateLicense(License license) {
        licenseRepository.save(license);

        license.add(
                linkTo(methodOn(LicenseController.class).createLicense(license)).withRel("createLicense"),
                linkTo(methodOn(LicenseController.class)
                        .getLicense(license.getOrganizationId(), license.getLicenseId(), "feign"))
                        .withRel("getLicense"),
                linkTo(methodOn(LicenseController.class).getLicenses(license.getOrganizationId()))
                        .withRel("getByOrganizationId"),
                linkTo(methodOn(LicenseController.class).updateLicense(license)).withSelfRel(),
                linkTo(methodOn(LicenseController.class).deleteLicense(
                        license.getLicenseId())).withRel("deleteLicense")
        );

        return license.withComment(serviceConfig.getExampleProperty());
    }

    public String deleteLicense(String licenseId) {
        licenseRepository.deleteById(licenseId);

        return String.format("Deleting license with id %s for the organization",
                licenseId);
    }

    public List<License> findAll() {
        return licenseRepository.findAll();
    }
}
