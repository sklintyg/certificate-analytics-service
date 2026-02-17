package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.PartyEntity;

public class PartyEntityMapper {

  private PartyEntityMapper() {
    throw new IllegalStateException("Utility class");
  }
  
  public static PartyEntity map(String party) {
    return PartyEntity.builder()
        .party(party)
        .build();
  }
}
