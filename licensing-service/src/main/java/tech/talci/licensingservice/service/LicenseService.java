package tech.talci.licensingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tech.talci.licensingservice.controller.LicenseController;
import tech.talci.licensingservice.domain.License;
import tech.talci.licensingservice.exceptions.ResourceNotFoundException;
import tech.talci.licensingservice.repositories.LicenseRepository;

import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class LicenseService {

    private final MessageSource messages;
    private final LicenseRepository licenseRepository;

    public License getLicense(String licenseId, String organizationId) {
        License license = licenseRepository.findLicenseByOrganizationIdAndLicenseId(organizationId,
                licenseId)
                .orElseThrow(() -> new ResourceNotFoundException("License was not found!"));


        license.add(
                linkTo(methodOn(LicenseController.class)
                        .getLicense(organizationId, license.getLicenseId())).withSelfRel(),
                linkTo(methodOn(LicenseController.class)
                        .createLicense(organizationId, license, null)).withRel("createLicense"),
                linkTo(methodOn(LicenseController.class)
                        .updateLicense(organizationId, license)).withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class)
                        .deleteLicense(organizationId, licenseId)).withRel("deleteLicense"));
        return license;
    }

    public String createLicense(License license, String organizationId, Locale locale) {
        String responseMessage = null;
        if(!StringUtils.isEmpty(license)) {
            license.setOrganizationId(organizationId);
            responseMessage = String .format(messages.getMessage("license.create.message", null, locale),
                    license.toString());
        }

        return responseMessage;
    }

    public String updateLicense(License license, String organizationId) {
        String responseMessage = null;
        if(!StringUtils.isEmpty(license)) {
            license.setOrganizationId(organizationId);
            responseMessage = String.format(messages.getMessage("license.update.message", null, null),
                    license.toString());
        }

        return responseMessage;
    }

    public String deleteLicense(String licenseId, String organizationId) {
        return String
                .format(messages.getMessage("license.delete.message", null, null), licenseId,
                        organizationId);
    }

}
