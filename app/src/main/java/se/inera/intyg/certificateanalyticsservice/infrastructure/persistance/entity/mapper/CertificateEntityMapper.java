package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.PseudonymizedAnalyticsMessage;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.CertificateEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.CertificateEntityRepository;

@Component
@RequiredArgsConstructor
public class CertificateEntityMapper {

  private final CertificateEntityRepository certificateEntityRepository;

  public CertificateEntity map(PseudonymizedAnalyticsMessage message) {
    return certificateEntityRepository.findByCertificateId(
            message.getCertificateId())
        .orElseGet(() ->
            certificateEntityRepository.save(CertificateEntity.builder()
                .certificateId(message.getCertificateId())
                .certificateType(message.getCertificateType())
                .certificateTypeVersion(message.getCertificateTypeVersion())
                .build()
            )
        );
  }
}