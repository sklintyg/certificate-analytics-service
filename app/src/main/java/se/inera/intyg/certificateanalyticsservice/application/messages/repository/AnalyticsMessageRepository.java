package se.inera.intyg.certificateanalyticsservice.application.messages.repository;

import se.inera.intyg.certificateanalyticsservice.application.messages.model.PseudonymizedAnalyticsMessage;

public interface AnalyticsMessageRepository {

  void save(PseudonymizedAnalyticsMessage message);

  PseudonymizedAnalyticsMessage findByMessageId(String messageId);

  void clear();
}
