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
package se.inera.intyg.certificateanalyticsservice.application.messages.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.inera.intyg.certificateanalyticsservice.infrastructure.logging.MdcHelper;
import se.inera.intyg.certificateanalyticsservice.infrastructure.logging.MdcLogConstants;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnalyticsMessageListener {

  private final AnalyticsMessageService analyticsMessageService;

  @Transactional
  @JmsListener(destination = "${certificate.analytics.message.queue.name}")
  public void onMessage(
      @Payload String body,
      @Header(name = "_type") String type,
      @Header(name = "schemaVersion", required = false) String schemaVersion,
      @Header(name = "sessionId", required = false) String sessionId,
      @Header(name = "traceId", required = false) String traceId,
      @Header(name = "messageId", required = false) String messageId) {
    try {
      MDC.put(MdcLogConstants.TRACE_ID_KEY, traceId == null ? MdcHelper.traceId() : traceId);
      MDC.put(MdcLogConstants.SPAN_ID_KEY, MdcHelper.spanId());
      MDC.put(MdcLogConstants.SESSION_ID_KEY, sessionId == null ? "-" : sessionId);

      analyticsMessageService.process(body, type, schemaVersion);
    } catch (Exception e) {
      log.error("Error processing analytics message with id '{}'", messageId, e);
      throw e;
    } finally {
      MDC.clear();
    }
  }
}
