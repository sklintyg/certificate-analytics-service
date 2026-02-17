package se.inera.intyg.certificateanalyticsservice.application.messages.model;

public interface AnalyticsMessagePseudonymizer {

  boolean canPseudonymize(CertificateAnalyticsMessage message);

  PseudonymizedAnalyticsMessage pseudonymize(CertificateAnalyticsMessage message);
}