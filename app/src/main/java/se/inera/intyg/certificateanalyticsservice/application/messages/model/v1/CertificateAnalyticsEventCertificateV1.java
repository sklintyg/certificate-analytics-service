package se.inera.intyg.certificateanalyticsservice.application.messages.model.v1;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.io.Serializable;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsEventCertificateV1.CertificateAnalyticsEventCertificateV1Builder;

@Value
@Builder
@JsonDeserialize(builder = CertificateAnalyticsEventCertificateV1Builder.class)
public class CertificateAnalyticsEventCertificateV1 implements Serializable {

  String id;
  String type;
  String typeVersion;
  String patientId;
  String unitId;
  String careProviderId;

  CertificateAnalyticsEventCertificateRelationV1 parent;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateAnalyticsEventCertificateV1Builder {

  }
}
