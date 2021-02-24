package tech.talci.organizationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.talci.organizationservice.model.Organization;
import tech.talci.organizationservice.service.OrganizationService;

@RestController
@RequestMapping(OrganizationController.BASE_URL)
@RequiredArgsConstructor
public class OrganizationController {
    public static final String BASE_URL = "/api/v1/organization";
    private final OrganizationService organizationService;

    @GetMapping("/{organizationId}")
    public ResponseEntity<Organization> getOrganization(@PathVariable String organizationId) {
        return ResponseEntity.ok(organizationService.findById(organizationId));
    }

    @PutMapping("/{organizationId}")
    public void updateOrganization(@PathVariable String organizationId,
                                   @RequestBody Organization organization) {
        organizationService.update(organization);
    }

    @PostMapping
    public ResponseEntity<Organization> create(@RequestBody Organization organization) {
        return ResponseEntity.ok(organizationService.create(organization));
    }

    @DeleteMapping("/{organizationId}")
    public void delete(@PathVariable String organizationId,
                       @RequestBody Organization organization) {
        organizationService.delete(organization);
    }
}
