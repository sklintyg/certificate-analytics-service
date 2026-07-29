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

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.AnalyticsMessageParser;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class CertificateAnalyticsEventV1Parser implements AnalyticsMessageParser {

  private static final String TYPE = "certificate.analytics.event";
  private static final String SCHEMA_VERSION = "v1";

  private final ObjectMapper objectMapper;

  public boolean canParse(String type, String schemaVersion) {
    return TYPE.equals(type) && SCHEMA_VERSION.equals(schemaVersion);
  }

  public CertificateAnalyticsMessageV1 parse(String message) {
    return objectMapper.readValue(message, CertificateAnalyticsMessageV1.class);
  }
}
