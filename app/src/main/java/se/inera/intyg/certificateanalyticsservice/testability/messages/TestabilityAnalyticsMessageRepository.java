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
package se.inera.intyg.certificateanalyticsservice.testability.messages;

import static se.inera.intyg.certificateanalyticsservice.testability.configuration.TestabilityConfiguration.TESTABILITY_PROFILE;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper.EventMapper;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.CareProviderEntityRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.CertificateEntityRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.EventEntityRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.JpaAnalyticsEventRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.MessageEntityRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.PartyEntityRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.RoleEntityRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.SessionEntityRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.UnitEntityRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.UserEntityRepository;

@Primary
@Repository
@Profile(TESTABILITY_PROFILE)
public class TestabilityAnalyticsMessageRepository extends JpaAnalyticsEventRepository {

  private final CertificateEntityRepository certificateEntityRepository;
  private final MessageEntityRepository messageEntityRepository;
  private final UserEntityRepository userEntityRepository;
  private final RoleEntityRepository roleEntityRepository;
  private final UnitEntityRepository unitEntityRepository;
  private final CareProviderEntityRepository careProviderEntityRepository;
  private final SessionEntityRepository sessionEntityRepository;
  private final PartyEntityRepository partyEntityRepository;

  public TestabilityAnalyticsMessageRepository(
      EventEntityRepository eventEntityRepository,
      EventMapper eventMapper,
      CertificateEntityRepository certificateEntityRepository,
      MessageEntityRepository messageEntityRepository,
      UserEntityRepository userEntityRepository,
      RoleEntityRepository roleEntityRepository,
      UnitEntityRepository unitEntityRepository,
      CareProviderEntityRepository careProviderEntityRepository,
      SessionEntityRepository sessionEntityRepository,
      PartyEntityRepository partyEntityRepository) {
    super(eventEntityRepository, eventMapper);
    this.certificateEntityRepository = certificateEntityRepository;
    this.messageEntityRepository = messageEntityRepository;
    this.userEntityRepository = userEntityRepository;
    this.roleEntityRepository = roleEntityRepository;
    this.unitEntityRepository = unitEntityRepository;
    this.careProviderEntityRepository = careProviderEntityRepository;
    this.sessionEntityRepository = sessionEntityRepository;
    this.partyEntityRepository = partyEntityRepository;
  }

  @Override
  @Transactional
  public void clear() {
    getEventEntityRepository().deleteAll();
    certificateEntityRepository.deleteAll();
    messageEntityRepository.deleteAll();
    userEntityRepository.deleteAll();
    roleEntityRepository.deleteAll();
    unitEntityRepository.deleteAll();
    careProviderEntityRepository.deleteAll();
    sessionEntityRepository.deleteAll();
    partyEntityRepository.deleteAll();
  }
}
