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
package se.inera.intyg.certificateanalyticsservice.application.messages.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.AnalyticsMessagePseudonymizer;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.CertificateAnalyticsMessage;

@Component
@RequiredArgsConstructor
public class AnalyticMessagePseudonymizerProvider {

  private final List<AnalyticsMessagePseudonymizer> analyticsMessagePseudonymizers;

  public AnalyticsMessagePseudonymizer pseudonymizer(CertificateAnalyticsMessage message) {
    return analyticsMessagePseudonymizers.stream()
        .filter(c -> c.canPseudonymize(message))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "No pseudonymizer found for message of class '%s'"
                        .formatted(message.getClass())));
  }
}
