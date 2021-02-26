package tech.talci.licensingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.talci.licensingservice.model.License;

import java.util.List;
import java.util.Optional;

public interface LicenseRepository extends JpaRepository<License, String> {

    List<License> findByOrganizationId(String organizationId);

    @Query("SELECT l FROM License l WHERE l.organizationId = ?1 AND l.licenseId = ?2")
    Optional<License> findByOrganizationIdAndLicenseId(String organizationId, String licenseId);
}
