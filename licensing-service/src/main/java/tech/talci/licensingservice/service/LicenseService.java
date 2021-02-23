package tech.talci.licensingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.talci.licensingservice.config.ServiceConfig;
import tech.talci.licensingservice.controller.LicenseController;
import tech.talci.licensingservice.model.License;
import tech.talci.licensingservice.repository.LicenseRepository;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class LicenseService {

    private final LicenseRepository licenseRepository;
    private final ServiceConfig serviceConfig;

    public License getLicense(String licenseId, String organizationId) {
        License license = licenseRepository
                .findByOrganizationIdAndLicenseId(licenseId, organizationId)
                .orElseThrow(() -> new IllegalArgumentException("License with " +
                        licenseId + " id was not found for a given organization ID: "
                        + organizationId));


        license.add(
                linkTo(methodOn(LicenseController.class).getLicense(organizationId, license.getLicenseId()))
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

    public License createLicense(License license) {
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);

        license.add(
                linkTo(methodOn(LicenseController.class).createLicense(license)).withSelfRel(),
                linkTo(methodOn(LicenseController.class)
                        .getLicense(license.getOrganizationId(), license.getLicenseId()))
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
                        .getLicense(license.getOrganizationId(), license.getLicenseId()))
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

}
