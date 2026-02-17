package se.inera.intyg.certificateanalyticsservice.application.messages.model.v1;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsEventV1.CertificateAnalyticsEventV1Builder;

@Value
@Builder
@JsonDeserialize(builder = CertificateAnalyticsEventV1Builder.class)
public class CertificateAnalyticsEventV1 implements Serializable {

  LocalDateTime timestamp;
  String messageType;
  String userId;
  String role;
  String unitId;
  String careProviderId;
  String origin;
  String sessionId;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateAnalyticsEventV1Builder {

  }
}
