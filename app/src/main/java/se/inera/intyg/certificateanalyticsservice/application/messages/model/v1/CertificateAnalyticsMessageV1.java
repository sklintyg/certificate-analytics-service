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
package se.inera.intyg.certificateanalyticsservice.application.messages.model.v1;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.CertificateAnalyticsMessage;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.v1.CertificateAnalyticsMessageV1.CertificateAnalyticsMessageV1Builder;

@Value
@Builder
@JsonDeserialize(builder = CertificateAnalyticsMessageV1Builder.class)
public class CertificateAnalyticsMessageV1 implements CertificateAnalyticsMessage {

  String messageId;
  String type;
  String schemaVersion;

  CertificateAnalyticsEventCertificateV1 certificate;
  CertificateAnalyticsEventV1 event;
  CertificateAnalyticsEventRecipientV1 recipient;
  CertificateAnalyticsEventMessageV1 message;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateAnalyticsMessageV1Builder {}
}
