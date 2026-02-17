package se.inera.intyg.certificateanalyticsservice.infrastructure.pseudonymization;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PseudonymizationTokenGenerator {

  @Value("${pseudonymization.key}")
  private byte[] key;

  @Value("${pseudonymization.context}")
  private String context;

  private static final String FIELD_ID = "id";
  private static final String FIELD_STAFF_ID = "staffId";
  private static final String FIELD_SESSION_ID = "sessionId";
  private static final String FIELD_CERTIFICATE_ID = "certificateId";
  private static final String FIELD_UNIT_ID = "unitId";
  private static final String FIELD_CARE_PROVIDER_ID = "careProviderId";
  private static final String FIELD_PATIENT_ID = "patientId";
  private static final String FIELD_MESSAGE_ID = "messageId";

  private static final String HMAC_SHA_256 = "HmacSHA256";
  private static final String INPUT_TEMPLATE = "%s|%s|%s";
  private static final int TOKEN_LENGTH = 16;

  public String id(String id) {
    return id == null ? null : token(
        FIELD_ID,
        normalize(id)
    );
  }

  public String eventUnitId(String eventUnitId) {
    return eventUnitId == null ? null : token(
        FIELD_UNIT_ID,
        normalize(eventUnitId)
    );
  }

  public String eventCareProviderId(String eventCareProviderId) {
    return eventCareProviderId == null ? null : token(
        FIELD_CARE_PROVIDER_ID,
        normalize(eventCareProviderId)
    );
  }

  public String staffId(String staffId) {
    return staffId == null ? null : token(
        FIELD_STAFF_ID,
        normalize(staffId)
    );
  }

  public String sessionId(String sessionId) {
    return sessionId == null ? null : token(
        FIELD_SESSION_ID,
        normalize(sessionId)
    );
  }

  public String certificateId(String certificateId) {
    return certificateId == null ? null : token(
        FIELD_CERTIFICATE_ID,
        normalize(certificateId)
    );
  }

  public String certificateUnitId(String certificateUnitId) {
    return certificateUnitId == null ? null : token(
        FIELD_UNIT_ID,
        normalize(certificateUnitId)
    );
  }

  public String certificateCareProviderId(String certificateCareProviderId) {
    return certificateCareProviderId == null ? null : token(
        FIELD_CARE_PROVIDER_ID,
        normalize(certificateCareProviderId)
    );
  }

  public String parentCertificateId(String certificateId) {
    return certificateId == null ? null : token(
        FIELD_CERTIFICATE_ID,
        normalize(certificateId)
    );
  }

  public String patientId(String patientId) {
    return patientId == null ? null : token(
        FIELD_PATIENT_ID,
        normalize(patientId)
    );
  }

  public String messageId(String messageId) {
    return messageId == null ? null : token(
        FIELD_MESSAGE_ID,
        normalize(messageId)
    );
  }

  public String messageAnswerId(String messageAnswerId) {
    return messageAnswerId == null ? null : token(
        FIELD_MESSAGE_ID,
        normalize(messageAnswerId)
    );
  }

  public String messageReminderId(String messageReminderId) {
    return messageReminderId == null ? null : token(
        FIELD_MESSAGE_ID,
        normalize(messageReminderId)
    );
  }

  private String normalize(String value) {
    return value == null ? null : value.trim().toLowerCase();
  }

  private String token(String field, String normalizedValue) {
    try {
      final var input = INPUT_TEMPLATE.formatted(field, context, normalizedValue);
      final var mac = Mac.getInstance(HMAC_SHA_256);
      mac.init(new SecretKeySpec(key, HMAC_SHA_256));
      final var hash = mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
      final var hash16Bytes = Arrays.copyOf(hash, TOKEN_LENGTH);
      return Base64.getUrlEncoder().withoutPadding().encodeToString(hash16Bytes);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}