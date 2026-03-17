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
