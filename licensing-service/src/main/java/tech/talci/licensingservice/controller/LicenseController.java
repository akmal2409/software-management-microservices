package tech.talci.licensingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tech.talci.licensingservice.model.License;
import tech.talci.licensingservice.service.LicenseService;

import java.util.List;

@Controller
@RequestMapping(value = LicenseController.BASE_URL)
@RequiredArgsConstructor
public class LicenseController {

    public static final String BASE_URL = "/api/v1/organization/{organizationId}/license";
    private final LicenseService licenseService;

    @GetMapping("/{licenseId}/{clientType}")
    public ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId,
                                              @PathVariable String licenseId,
                                              @PathVariable String clientType) {
        License license = licenseService.getLicense(licenseId, organizationId, clientType);
        return ResponseEntity.ok(license);
    }

    @GetMapping
    public ResponseEntity<List<License>> findAll() {
        return ResponseEntity.ok(licenseService.findAll());
    }

    @PutMapping
    public ResponseEntity<License> updateLicense(@RequestBody License license) {
        return ResponseEntity.ok(licenseService.updateLicense(license));
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestBody License license) {
        return ResponseEntity.ok(licenseService.createLicense(license));
    }

    @DeleteMapping("/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable String licenseId) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
    }
}
