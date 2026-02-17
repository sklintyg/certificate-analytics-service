package se.inera.intyg.certificateanalyticsservice.application.messages.model.v1;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.CertificateAnalyticsMessage;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsMessageV1.CertificateAnalyticsMessageV1Builder;

@Value
@Builder
@JsonDeserialize(builder = CertificateAnalyticsMessageV1Builder.class)
public class CertificateAnalyticsMessageV1 implements CertificateAnalyticsMessage {

  String messageId;
  String type;
  String schemaVersion;

  CertificateAnalyticsEventCertificateV1 certificate;
  CertificateAnalyticsEventV1 event;
  CertificateAnalyticsEventRecipientV1 recipient;
  CertificateAnalyticsEventMessageV1 message;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateAnalyticsMessageV1Builder {

  }
}
