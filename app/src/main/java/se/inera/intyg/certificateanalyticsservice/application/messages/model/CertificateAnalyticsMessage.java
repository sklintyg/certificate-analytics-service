package se.inera.intyg.certificateanalyticsservice.application.messages.model;

import java.io.Serializable;

public interface CertificateAnalyticsMessage extends Serializable {

  String getMessageId();

  String getType();

  String getSchemaVersion();
}
