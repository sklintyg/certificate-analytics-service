package se.inera.intyg.certificateanalyticsservice.application.messages.model;

public interface AnalyticsMessageParser {

  boolean canParse(String type, String schemaVersion);

  CertificateAnalyticsMessage parse(String message);
}
