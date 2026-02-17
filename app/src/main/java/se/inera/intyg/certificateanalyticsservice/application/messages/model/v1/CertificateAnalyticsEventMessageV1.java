package se.inera.intyg.certificateanalyticsservice.application.messages.model.v1;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsEventMessageV1.CertificateAnalyticsEventMessageV1Builder;

@Value
@Builder
@JsonDeserialize(builder = CertificateAnalyticsEventMessageV1Builder.class)
public class CertificateAnalyticsEventMessageV1 implements Serializable {

  String id;
  String answerId;
  String reminderId;

  String type;

  LocalDateTime sent;
  LocalDate lastDateToAnswer;

  List<String> questionIds;

  String sender;
  String recipient;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateAnalyticsEventMessageV1Builder {

  }
}
