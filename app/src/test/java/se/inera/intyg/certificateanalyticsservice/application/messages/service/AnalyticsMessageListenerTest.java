package se.inera.intyg.certificateanalyticsservice.application.messages.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.draftMessageBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.toJson;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

@ExtendWith(MockitoExtension.class)
class AnalyticsMessageListenerTest {

  @Mock
  private AnalyticsMessageService analyticsMessageService;
  @InjectMocks
  private AnalyticsMessageListener analyticsMessageListener;

  @Test
  void shallDelegateToServiceToProcessMessage() {
    final var message = draftMessageBuilder().build();
    final var messageAsJson = toJson(message);

    analyticsMessageListener.onMessage(messageAsJson, message.getType(), message.getSchemaVersion(),
        null, null, null);

    verify(analyticsMessageService).process(messageAsJson, message.getType(),
        message.getSchemaVersion());
  }

  @Test
  void shallThrowExceptionWhenProcessingFailsWithException() {
    final var message = draftMessageBuilder().build();
    final var messageAsJson = toJson(message);
    final var type = message.getType();
    final var schemaVersion = message.getSchemaVersion();

    doThrow(RuntimeException.class).when(analyticsMessageService)
        .process(messageAsJson, type, schemaVersion);

    assertThrows(RuntimeException.class, () ->
        analyticsMessageListener.onMessage(messageAsJson, type, schemaVersion, null, null, null)
    );
  }

  @Test
  void shallDecorateMdcWithEmptySessionId() {
    final var expected = "-";
    final var message = draftMessageBuilder().build();
    final var messageAsJson = toJson(message);

    try (MockedStatic<MDC> mocked = mockStatic(MDC.class)) {
      analyticsMessageListener.onMessage(messageAsJson, message.getType(),
          message.getSchemaVersion(), null, null, null);
      mocked.verify(() -> MDC.put("session.id", expected));
    }
  }

  @Test
  void shallDecorateMdcWithHeaderSessionId() {
    final var expected = "my-session-id";
    final var message = draftMessageBuilder().build();
    final var messageAsJson = toJson(message);

    try (MockedStatic<MDC> mocked = mockStatic(MDC.class)) {
      analyticsMessageListener.onMessage(messageAsJson, message.getType(),
          message.getSchemaVersion(), expected, null, null);
      mocked.verify(() -> MDC.put("session.id", expected));
    }
  }

  @Test
  void shallDecorateMdcWithGeneratedTraceId() {
    final var message = draftMessageBuilder().build();
    final var messageAsJson = toJson(message);

    try (MockedStatic<MDC> mocked = mockStatic(MDC.class)) {
      analyticsMessageListener.onMessage(messageAsJson, message.getType(),
          message.getSchemaVersion(), null, null, null);
      mocked.verify(() -> MDC.put(eq("trace.id"), anyString()));
    }
  }

  @Test
  void shallDecorateMdcWithHeaderTraceId() {
    final var expected = "my-trace-id";
    final var message = draftMessageBuilder().build();
    final var messageAsJson = toJson(message);

    try (MockedStatic<MDC> mocked = mockStatic(MDC.class)) {
      analyticsMessageListener.onMessage(messageAsJson, message.getType(),
          message.getSchemaVersion(), null, expected, null);
      mocked.verify(() -> MDC.put("trace.id", expected));
    }
  }

  @Test
  void shallDecorateMdcWithGeneratedSpanId() {
    final var message = draftMessageBuilder().build();
    final var messageAsJson = toJson(message);

    try (MockedStatic<MDC> mocked = mockStatic(MDC.class)) {
      analyticsMessageListener.onMessage(messageAsJson, message.getType(),
          message.getSchemaVersion(), null, null, null);
      mocked.verify(() -> MDC.put(eq("span.id"), anyString()));
    }
  }

  @Test
  void shallClearMdcWhenProcessingSucceeds() {
    final var message = draftMessageBuilder().build();
    final var messageAsJson = toJson(message);

    analyticsMessageListener.onMessage(messageAsJson, message.getType(),
        message.getSchemaVersion(), null, null, null);

    assertEquals(0, MDC.getCopyOfContextMap() == null ? 0 : MDC.getCopyOfContextMap().size());
  }

  @Test
  void shallClearMdcWhenProcessingFails() {
    final var message = draftMessageBuilder().build();
    final var messageAsJson = toJson(message);
    final var type = message.getType();
    final var schemaVersion = message.getSchemaVersion();
    
    doThrow(RuntimeException.class).when(analyticsMessageService)
        .process(messageAsJson, type, schemaVersion);

    assertThrows(RuntimeException.class, () ->
        analyticsMessageListener.onMessage(messageAsJson, type, schemaVersion, null, null, null)
    );

    assertEquals(0, MDC.getCopyOfContextMap() == null ? 0 : MDC.getCopyOfContextMap().size());
  }
}