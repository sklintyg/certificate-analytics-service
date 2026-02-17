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
      @Header(name = "messageId", required = false) String messageId
  ) {
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
