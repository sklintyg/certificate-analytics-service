package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dim_certificate_type")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificateTypeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "certificate_type_key")
  private Long key;

  @Column(name = "certificate_type", nullable = false, length = 20)
  private String certificateType;

  @Column(name = "certificate_type_version", nullable = false, length = 20)
  private String certificateTypeVersion;
}
