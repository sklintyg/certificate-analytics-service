package se.inera.intyg.certificateanalyticsservice.application.messages.model.v1;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.io.Serializable;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsEventCertificateRelationV1.CertificateAnalyticsEventCertificateRelationV1Builder;

@Value
@Builder
@JsonDeserialize(builder = CertificateAnalyticsEventCertificateRelationV1Builder.class)
public class CertificateAnalyticsEventCertificateRelationV1 implements Serializable {

  String id;
  String type;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateAnalyticsEventCertificateRelationV1Builder {

  }
}
