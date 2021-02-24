package tech.talci.licensingservice.service.clients;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tech.talci.licensingservice.model.Organization;

@Component
public class OrganizationRestTemplateClient {

    final RestTemplate restTemplate;

    public OrganizationRestTemplateClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Organization getOrganization(String organizationId) {
        ResponseEntity<Organization> restExchange =
                restTemplate.exchange("http://organization-service/api/v1/organization/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId);
        return restExchange.getBody();
    }
}
