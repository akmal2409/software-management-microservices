package tech.talci.organizationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.talci.organizationservice.model.Organization;
import tech.talci.organizationservice.repository.OrganizationRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public Organization findById(String organizationId) {
        return organizationRepository.findById(organizationId)
                .orElseGet(null);
    }

    public Organization create(Organization organization) {
        organization.setOrganizationId(UUID.randomUUID().toString());
        organization = organizationRepository.save(organization);
        return organization;
    }

    public void update(Organization organization) {
        organizationRepository.save(organization);
    }

    public void delete(Organization organization) {
        organizationRepository.deleteById(organization.getOrganizationId());
    }
}
