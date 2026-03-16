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
package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.PseudonymizedAnalyticsMessage;
import se.inera.intyg.certificateanalyticsservice.application.messages.repository.AnalyticsMessageRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper.EventMapper;

@Repository
@RequiredArgsConstructor
public class JpaAnalyticsEventRepository implements AnalyticsMessageRepository {

  private final EventEntityRepository eventEntityRepository;
  private final EventMapper eventMapper;

  public void save(PseudonymizedAnalyticsMessage message) {
    final var entity = eventMapper.toEntity(message);
    eventEntityRepository.save(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public PseudonymizedAnalyticsMessage findByMessageId(String messageId) {
    return eventEntityRepository.findByMessageId(messageId).map(eventMapper::toDomain).orElse(null);
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException("clear() is only supported in testability profile");
  }

  protected EventEntityRepository getEventEntityRepository() {
    return eventEntityRepository;
  }
}
