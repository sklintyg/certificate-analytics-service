/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.certificateanalyticsservice.testdata;

import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CARE_PROVIDER_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CERTIFICATE_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CERTIFICATE_PARENT_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CERTIFICATE_PARENT_TYPE;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CERTIFICATE_TYPE;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CERTIFICATE_TYPE_VERSION;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.EVENT_TIMESTAMP;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.EVENT_TYPE_CERTIFICATE_SENT;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.EVENT_TYPE_COMPLEMENT_FROM_RECIPIENT;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.EVENT_TYPE_DRAFT_CREATED;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_ANSWER_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_ID_CREATED;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_LAST_DATE_TO_ANSWER;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_QUESTION_ID_1;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_QUESTION_ID_2;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_RECIPIENT;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_REMINDER_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_SENDER;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_SENT;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_TYPE;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.ORIGIN;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.PATIENT_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.PRIVATE_PRACTITIONER_CARE_PROVIDER_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.PRIVATE_PRACTITIONER_UNIT_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.RECIPIENT;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.ROLE;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.SCHEMA_VERSION;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.SESSION_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.TYPE_ANALYTICS_EVENT;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.UNIT_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.USER_ID;

import java.time.LocalDateTime;
import java.util.List;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsEventCertificateRelationV1;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsEventCertificateRelationV1.CertificateAnalyticsEventCertificateRelationV1Builder;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsEventCertificateV1;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsEventCertificateV1.CertificateAnalyticsEventCertificateV1Builder;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsEventMessageV1;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsEventRecipientV1;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsEventV1;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsEventV1.CertificateAnalyticsEventV1Builder;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsMessageV1;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsMessageV1.CertificateAnalyticsMessageV1Builder;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

public class TestDataMessages {

  private static final ObjectMapper OBJECT_MAPPER = build();

  private TestDataMessages() {
    throw new IllegalStateException("Utility class");
  }

  private static ObjectMapper build() {
    return JsonMapper.builder().build();
  }

  public static String toJson(CertificateAnalyticsMessageV1 message) {
    return OBJECT_MAPPER.writeValueAsString(message);
  }

  public static CertificateAnalyticsMessageV1Builder replaceMessageBuilder() {
    return CertificateAnalyticsMessageV1.builder()
        .messageId(MESSAGE_ID_CREATED)
        .type(TYPE_ANALYTICS_EVENT)
        .schemaVersion(SCHEMA_VERSION)
        .certificate(sentCertificateBuilder().build())
        .event(draftEventBuilder().build())
        .recipient(CertificateAnalyticsEventRecipientV1.builder().id(RECIPIENT).build());
  }

  public static CertificateAnalyticsMessageV1Builder sentMessageBuilder() {
    return CertificateAnalyticsMessageV1.builder()
        .messageId(MESSAGE_ID_CREATED)
        .type(TYPE_ANALYTICS_EVENT)
        .schemaVersion(SCHEMA_VERSION)
        .certificate(sentCertificateBuilder().build())
        .event(
            eventBuilder()
                .messageType(EVENT_TYPE_CERTIFICATE_SENT)
                .timestamp(LocalDateTime.parse(EVENT_TIMESTAMP))
                .userId(USER_ID)
                .role(ROLE)
                .unitId(UNIT_ID)
                .careProviderId(CARE_PROVIDER_ID)
                .sessionId(SESSION_ID)
                .origin(ORIGIN)
                .build())
        .recipient(CertificateAnalyticsEventRecipientV1.builder().id(RECIPIENT).build());
  }

  public static CertificateAnalyticsEventCertificateV1Builder sentCertificateBuilder() {
    return certificateBuilder()
        .id(CERTIFICATE_ID)
        .unitId(UNIT_ID)
        .careProviderId(CARE_PROVIDER_ID)
        .patientId(PATIENT_ID)
        .type(CERTIFICATE_TYPE)
        .typeVersion(CERTIFICATE_TYPE_VERSION)
        .parent(replacedRelationBuilder().build());
  }

  public static CertificateAnalyticsEventCertificateRelationV1Builder replacedRelationBuilder() {
    return CertificateAnalyticsEventCertificateRelationV1.builder()
        .id(CERTIFICATE_PARENT_ID)
        .type(CERTIFICATE_PARENT_TYPE);
  }

  public static CertificateAnalyticsMessageV1Builder draftPrivatePractitionerMessageBuilder() {
    return draftMessageBuilder()
        .event(
            draftEventBuilder()
                .unitId(PRIVATE_PRACTITIONER_UNIT_ID)
                .careProviderId(PRIVATE_PRACTITIONER_CARE_PROVIDER_ID)
                .build())
        .certificate(
            draftCertificateBuilder()
                .unitId(PRIVATE_PRACTITIONER_UNIT_ID)
                .careProviderId(PRIVATE_PRACTITIONER_CARE_PROVIDER_ID)
                .build());
  }

  public static CertificateAnalyticsMessageV1Builder draftMessageBuilder() {
    return CertificateAnalyticsMessageV1.builder()
        .messageId(MESSAGE_ID_CREATED)
        .type(TYPE_ANALYTICS_EVENT)
        .schemaVersion(SCHEMA_VERSION)
        .certificate(
            certificateBuilder()
                .id(CERTIFICATE_ID)
                .unitId(UNIT_ID)
                .careProviderId(CARE_PROVIDER_ID)
                .patientId(PATIENT_ID)
                .type(CERTIFICATE_TYPE)
                .typeVersion(CERTIFICATE_TYPE_VERSION)
                .build())
        .event(draftEventBuilder().build());
  }

  public static CertificateAnalyticsEventV1Builder draftEventBuilder() {
    return eventBuilder()
        .messageType(EVENT_TYPE_DRAFT_CREATED)
        .timestamp(LocalDateTime.parse(EVENT_TIMESTAMP))
        .userId(USER_ID)
        .role(ROLE)
        .unitId(UNIT_ID)
        .careProviderId(CARE_PROVIDER_ID)
        .sessionId(SESSION_ID)
        .origin(ORIGIN);
  }

  public static CertificateAnalyticsEventCertificateV1Builder draftCertificateBuilder() {
    return certificateBuilder()
        .id(CERTIFICATE_ID)
        .unitId(UNIT_ID)
        .careProviderId(CARE_PROVIDER_ID)
        .patientId(PATIENT_ID)
        .type(CERTIFICATE_TYPE)
        .typeVersion(CERTIFICATE_TYPE_VERSION);
  }

  public static CertificateAnalyticsMessageV1Builder receivedQuestionMessageBuilder() {
    return CertificateAnalyticsMessageV1.builder()
        .messageId(MESSAGE_ID_CREATED)
        .type(TYPE_ANALYTICS_EVENT)
        .schemaVersion(SCHEMA_VERSION)
        .certificate(
            certificateBuilder()
                .id(CERTIFICATE_ID)
                .unitId(UNIT_ID)
                .careProviderId(CARE_PROVIDER_ID)
                .patientId(PATIENT_ID)
                .type(CERTIFICATE_TYPE)
                .typeVersion(CERTIFICATE_TYPE_VERSION)
                .build())
        .event(
            eventBuilder()
                .messageType(EVENT_TYPE_COMPLEMENT_FROM_RECIPIENT)
                .timestamp(LocalDateTime.parse(EVENT_TIMESTAMP))
                .userId(USER_ID)
                .role(ROLE)
                .unitId(UNIT_ID)
                .careProviderId(CARE_PROVIDER_ID)
                .sessionId(SESSION_ID)
                .origin(ORIGIN)
                .build())
        .recipient(CertificateAnalyticsEventRecipientV1.builder().id(RECIPIENT).build())
        .message(
            CertificateAnalyticsEventMessageV1.builder()
                .id(MESSAGE_ID)
                .answerId(MESSAGE_ANSWER_ID)
                .reminderId(MESSAGE_REMINDER_ID)
                .type(MESSAGE_TYPE)
                .sender(MESSAGE_SENDER)
                .recipient(MESSAGE_RECIPIENT)
                .questionIds(List.of(MESSAGE_QUESTION_ID_1, MESSAGE_QUESTION_ID_2))
                .sent(MESSAGE_SENT)
                .lastDateToAnswer(MESSAGE_LAST_DATE_TO_ANSWER)
                .build());
  }

  private static CertificateAnalyticsEventCertificateV1Builder certificateBuilder() {
    return CertificateAnalyticsEventCertificateV1.builder()
        .id(CERTIFICATE_ID)
        .unitId(UNIT_ID)
        .careProviderId(CARE_PROVIDER_ID)
        .patientId(PATIENT_ID)
        .type(CERTIFICATE_TYPE)
        .typeVersion(CERTIFICATE_TYPE_VERSION);
  }

  private static CertificateAnalyticsEventV1Builder eventBuilder() {
    return CertificateAnalyticsEventV1.builder()
        .timestamp(LocalDateTime.parse(EVENT_TIMESTAMP))
        .userId(USER_ID)
        .role(ROLE)
        .unitId(UNIT_ID)
        .careProviderId(CARE_PROVIDER_ID)
        .sessionId(SESSION_ID)
        .origin(ORIGIN);
  }
}
