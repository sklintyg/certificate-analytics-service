package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.PartyEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper.PartyEntityMapper;

@Repository
@RequiredArgsConstructor
public class PartyRepository {

  private final PartyEntityRepository partyEntityRepository;

  public PartyEntity findOrCreate(String party) {
    if (party == null || party.isBlank()) {
      return null;
    }
    return partyEntityRepository.findByParty(party)
        .orElseGet(() -> partyEntityRepository.save(PartyEntityMapper.map(party)));
  }
}
