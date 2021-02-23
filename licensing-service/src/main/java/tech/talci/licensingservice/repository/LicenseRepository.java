package tech.talci.licensingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.talci.licensingservice.model.License;

import java.util.List;
import java.util.Optional;

public interface LicenseRepository extends JpaRepository<License, String> {

    List<License> findByOrganizationId(String organizationId);
    Optional<License> findByOrganizationIdAndLicenseId(String organizationId, String licenseId);
}
