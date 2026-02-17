package se.inera.intyg.certificateanalyticsservice.infrastructure.logging;

public class MdcLogConstants {

  private MdcLogConstants() {
  }

  public static final String SESSION_ID_KEY = "session.id";
  public static final String SPAN_ID_KEY = "span.id";
  public static final String TRACE_ID_KEY = "trace.id";
}