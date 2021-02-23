package tech.talci.licensingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tech.talci.licensingservice.controller.LicenseController;
import tech.talci.licensingservice.model.License;

import java.util.Random;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class LicenseService {

    public License getLicense(String licenseId, String organizationId) {
        License license = License.builder()
                .id(new Random().nextLong())
                .licenseId(licenseId)
                .organizationId(organizationId)
                .description("Software Product")
                .productName("TalciSoftware")
                .licenseType("full")
                .build();

        license.add(
                linkTo(methodOn(LicenseController.class).getLicense(organizationId, license.getLicenseId()))
                .withSelfRel(),
                linkTo(methodOn(LicenseController.class).createLicense(organizationId, license))
                .withRel("createLicense"),
                linkTo(methodOn(LicenseController.class).updateLicense(organizationId, license))
                        .withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class).deleteLicense(organizationId, license.getLicenseId()))
                .withRel("deleteLicense")
        );

        return license;
    }

    public String createLicense(License license, String organizationId) {
        String responseMessage = null;
        if(!StringUtils.isEmpty(license)) {
            license.setOrganizationId(organizationId);
            responseMessage = String.format("This is the post and the object is: %s", license.toString());
        }

        return responseMessage;
    }

    public String updateLicense(License license, String organizationId) {
        String responseMessage = null;
        if (!StringUtils.isEmpty(license)) {
            license.setOrganizationId(organizationId);
            responseMessage = String.format("This is the put and the object is: %s", license.toString());
        }

        return responseMessage;
    }

    public String deleteLicense(String licenseId, String organizationId) {
        String responseMessage = null;
        responseMessage = String.format("Deleting license with id %s for the organization %s",
                licenseId, organizationId);
        return responseMessage;
    }
}
