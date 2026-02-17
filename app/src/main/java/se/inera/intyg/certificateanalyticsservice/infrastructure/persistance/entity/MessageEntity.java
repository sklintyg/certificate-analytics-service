package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dim_message")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "message_key")
  private Long key;

  @Column(name = "message_id", length = 22, nullable = false, unique = true)
  private String messageId;

  @Column(name = "message_answer_id", length = 22)
  private String messageAnswerId;

  @Column(name = "message_reminder_id", length = 22)
  private String messageReminderId;

  @Column(name = "message_type", length = 24, nullable = false)
  private String messageType;

  @Column(name = "sent", nullable = false)
  private LocalDateTime sent;

  @Column(name = "last_date_to_answer")
  private LocalDate lastDateToAnswer;

  @Column(name = "complement_first_question_id", length = 32)
  private String complementFirstQuestionId;

  @Column(name = "complement_second_question_id", length = 32)
  private String complementSecondQuestionId;

  @Column(name = "complement_third_question_id", length = 32)
  private String complementThirdQuestionId;

  @Column(name = "complement_fourth_question_id", length = 32)
  private String complementFourthQuestionId;

  @Column(name = "complement_fifth_question_id", length = 32)
  private String complementFifthQuestionId;

  @Column(name = "complement_sixth_question_id", length = 32)
  private String complementSixthQuestionId;

  @Column(name = "complement_seventh_question_id", length = 32)
  private String complementSeventhQuestionId;

}
