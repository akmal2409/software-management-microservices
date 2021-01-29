package tech.talci.licensingservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.talci.licensingservice.domain.License;

import java.util.List;
import java.util.Optional;

public interface LicenseRepository extends JpaRepository<License, String> {
    List<License> findLicenseByOrganizationId(String organizationId);
    Optional<License> findLicenseByOrganizationIdAndLicenseId(String organizationId,
                                                     String licenseId);
}
