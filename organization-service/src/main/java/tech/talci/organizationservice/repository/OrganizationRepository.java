package tech.talci.organizationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.talci.organizationservice.model.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, String> {
}
