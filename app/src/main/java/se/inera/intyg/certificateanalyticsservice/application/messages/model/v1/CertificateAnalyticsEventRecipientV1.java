package se.inera.intyg.certificateanalyticsservice.application.messages.model.v1;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.io.Serializable;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsEventRecipientV1.CertificateAnalyticsEventRecipientV1Builder;

@Value
@Builder
@JsonDeserialize(builder = CertificateAnalyticsEventRecipientV1Builder.class)
public class CertificateAnalyticsEventRecipientV1 implements Serializable {

  String id;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateAnalyticsEventRecipientV1Builder {

  }
}
