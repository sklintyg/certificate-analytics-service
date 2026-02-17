package se.inera.intyg.certificateanalyticsservice.testdata;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class TestDataConstants {

  public static final String MESSAGE_ID_CREATED = "50d64e86-9226-4795-aadd-c00c084c030d";
  public static final String CERTIFICATE_ID = "78a0a279-1197-4cc7-bf6a-899cb5034053";
  public static final String CERTIFICATE_TYPE = "fk7210";
  public static final String CERTIFICATE_TYPE_VERSION = "1.0";
  public static final String CERTIFICATE_PARENT_ID = "9a6e4f30-3a4e-4637-be91-ee5213b89bad";
  public static final String CERTIFICATE_PARENT_TYPE = "REPLACED";
  public static final String UNIT_ID = "TSTNMT2321000156-ALMC";
  public static final String CARE_PROVIDER_ID = "TSTNMT2321000156-ALFA";
  public static final String PRIVATE_PRACTITIONER_UNIT_ID = "TSTNMT2321000156-WEBCERT99999";
  public static final String PRIVATE_PRACTITIONER_CARE_PROVIDER_ID = "TSTNMT2321000156-WEBCERT99999";
  public static final String USER_ID = "TSTNMT2321000156-DRAA";
  public static final String PATIENT_ID = "19401130-6125";
  public static final String ROLE = "LAKARE";
  public static final String ORIGIN = "NORMAL";
  public static final String SESSION_ID = "2d02bc34-41f1-42b7-9964-d0659bf369c8";
  public static final LocalDateTime TIMESTAMP = LocalDateTime.of(2025, 9, 29, 17, 49, 58);
  public static final String RECIPIENT = "FKASSA";
  public static final String SCHEMA_VERSION = "v1";
  public static final String TYPE_ANALYTICS_EVENT = "certificate.analytics.event";
  public static final String EVENT_TYPE_DRAFT_CREATED = "DRAFT_CREATED";
  public static final String EVENT_TYPE_CERTIFICATE_SENT = "CERTIFICATE_SENT";
  public static final String EVENT_TYPE_COMPLEMENT_FROM_RECIPIENT = "COMPLEMENT_FROM_RECIPIENT";
  public static final String EVENT_TIMESTAMP = "2025-09-29T17:49:58";

  public static final String MESSAGE_ID = "message-12345";
  public static final String MESSAGE_ANSWER_ID = "answer-67890";
  public static final String MESSAGE_REMINDER_ID = "reminder-54321";
  public static final String MESSAGE_TYPE = "KOMPLT";
  public static final String MESSAGE_SENDER = "FKASSA";
  public static final String MESSAGE_RECIPIENT = "HSVARD";
  public static final String MESSAGE_QUESTION_ID_1 = "question-1";
  public static final String MESSAGE_QUESTION_ID_2 = "question-2";
  public static final LocalDateTime MESSAGE_SENT = LocalDateTime.parse("2025-10-01T10:15:30");
  public static final LocalDate MESSAGE_LAST_DATE_TO_ANSWER = LocalDate.parse("2025-11-01");

  public static final String HASHED_ID = "FVs8LGHLwIs2WP0q1A5bkA";
  public static final String HASHED_CERTIFICATE_ID = "Xsg4sVYtNq_zMGU_wWrJgw";
  public static final String HASHED_CERTIFICATE_PARENT_ID = "VDJhjt69htHErDZl21BM7w";
  public static final String HASHED_PATIENT_ID = "v4WI46Ymy08FKdhJJFDocw";
  public static final String HASHED_USER_ID = "IlXi3vfzsRwLaNRjpqYxOQ";
  public static final String HASHED_PRIVATE_PRACTITIONER_UNIT_ID = "xStOJ2OE_hQtBFLdsn-hFg";
  public static final String HASHED_PRIVATE_PRACTITIONER_CARE_PROVIDER_ID = "3ULOCud84JbSZDdQ7qUDFg";
  public static final String HASHED_SESSION_ID = "GRmmGqqMdm6mFSy9ZCfT5w";
  public static final String HASHED_MESSAGE_ID = "6Z8etRcPCMrOc8Q629-9LA";
  public static final String HASHED_MESSAGE_ANSWER_ID = "xzojWDGPoK1OBj047LRK2Q";
  public static final String HASHED_MESSAGE_REMINDER_ID = "9q_f1G8muLQuzm-4yfhcgg";

  private TestDataConstants() {
  }
}