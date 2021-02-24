package tech.talci.licensingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.talci.licensingservice.config.ServiceConfig;
import tech.talci.licensingservice.controller.LicenseController;
import tech.talci.licensingservice.model.License;
import tech.talci.licensingservice.model.Organization;
import tech.talci.licensingservice.repository.LicenseRepository;
import tech.talci.licensingservice.service.clients.OrganizationFeignClient;
import tech.talci.licensingservice.service.clients.OrganizationRestTemplateClient;

import java.util.List;
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
    private final OrganizationRestTemplateClient restTemplateClient;

    public License getLicense(String licenseId, String organizationId, String clientType) {
        License license = licenseRepository
                .findByOrganizationIdAndLicenseId(licenseId, organizationId)
                .orElseThrow(() -> new IllegalArgumentException("License with " +
                        licenseId + " id was not found for a given organization ID: "
                        + organizationId));

        Organization organization = getOrganizationInfo(organizationId, clientType);

        license.add(
                linkTo(methodOn(LicenseController.class).getLicense(organizationId, license.getLicenseId(), clientType))
                        .withSelfRel(),
                linkTo(methodOn(LicenseController.class).createLicense(license))
                        .withRel("createLicense"),
                linkTo(methodOn(LicenseController.class).updateLicense(license))
                        .withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class).deleteLicense(license.getLicenseId()))
                        .withRel("deleteLicense")
        );

        return license.withComment(serviceConfig.getExampleProperty());
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
