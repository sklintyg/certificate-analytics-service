package se.inera.intyg.certificateanalyticsservice.application.messages.service;

public interface AnalyticsMessageService {

  void process(String body, String type, String schemaVersion);
}
