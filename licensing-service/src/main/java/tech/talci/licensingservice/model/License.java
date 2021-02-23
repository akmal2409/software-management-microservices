package tech.talci.licensingservice.model;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "licenses")
public class License extends RepresentationModel<License> {

    @Id
    @Column(name = "license_id", nullable = false)
    private String licenseId;
    private String description;
    @Column(name = "organization_id", nullable = false)
    private String organizationId;
    @Column(name = "product_name", nullable = false)
    private String productName;
    @Column(name = "license_type", nullable = false)
    private String licenseType;
    @Column(name = "comment")
    private String comment;

    public License withComment(String comment) {
        this.comment = comment;
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String licenseId;
        private String description;
        private String organizationId;
        private String productName;
        private String licenseType;
        private String comment;

        public Builder licenseId(String licenseId) {
            this.licenseId = licenseId;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder licenseType(String licenseType) {
            this.licenseType = licenseType;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public License build() {
            return new License(licenseId, description,
                    organizationId, productName, licenseType, comment);
        }
    }
}
