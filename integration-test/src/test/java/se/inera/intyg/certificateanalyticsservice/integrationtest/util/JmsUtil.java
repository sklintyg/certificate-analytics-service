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
package se.inera.intyg.certificateanalyticsservice.integrationtest.util;

import static org.awaitility.Awaitility.await;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.toJson;

import jakarta.jms.Message;
import java.time.Duration;
import java.util.function.Predicate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsMessageV1;
import se.inera.intyg.certificateanalyticsservice.infrastructure.logging.MdcHelper;

public class JmsUtil {

  public static final String DLQ_QUEUE_NAME = "ActiveMQ.DLQ";

  private final JmsTemplate jmsTemplate;
  private final String queueName;

  public JmsUtil(JmsTemplate jmsTemplate, String queueName) {
    this.jmsTemplate = jmsTemplate;
    this.queueName = queueName;
  }

  public void reset() {
    purgeQueue(queueName);
    purgeQueue(DLQ_QUEUE_NAME);
  }

  public void publishMessage(CertificateAnalyticsMessageV1 message) {
    jmsTemplate.convertAndSend(queueName, toJson(message), messagePostProcessor(message));
  }

  public void publishUnparsableMessage(CertificateAnalyticsMessageV1 message) {
    jmsTemplate.convertAndSend(queueName, "unparsableMessage", messagePostProcessor(message));
  }

  public boolean awaitProcessedToDlq(String messageId, Duration timeout) {
    await()
        .atMost(timeout)
        .pollInterval(Duration.ofMillis(200))
        .until(() -> dlqContains(messageId));
    return true;
  }

  private void purgeQueue(String queueName) {
    jmsTemplate.setReceiveTimeout(100);
    while (true) {
      final var msg = jmsTemplate.receive(queueName);
      if (msg == null) {
        break;
      }
    }
  }

  private static MessagePostProcessor messagePostProcessor(CertificateAnalyticsMessageV1 message) {
    return msg -> {
      msg.setStringProperty("messageId", message.getMessageId());
      msg.setStringProperty("sessionId", message.getEvent().getSessionId());
      msg.setStringProperty("traceId", MdcHelper.traceId());
      msg.setStringProperty("_type", message.getType());
      msg.setStringProperty("schemaVersion", message.getSchemaVersion());
      msg.setStringProperty("contentType", "application/json");
      msg.setStringProperty("messageType", message.getEvent().getMessageType());
      return msg;
    };
  }

  private boolean dlqContains(String messageId) {
    final var pred = matchingMessageIdPredicate(messageId);
    return Boolean.TRUE.equals(
        jmsTemplate.browse(
            DLQ_QUEUE_NAME,
            (session, browser) -> {
              var e = browser.getEnumeration();
              while (e.hasMoreElements()) {
                final var m = (Message) e.nextElement();
                if (pred.test(m)) {
                  return true;
                }
              }
              return false;
            }));
  }

  private Predicate<Message> matchingMessageIdPredicate(String messageId) {
    return m -> {
      try {
        return messageId.equals(m.getStringProperty("messageId"));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    };
  }
}
