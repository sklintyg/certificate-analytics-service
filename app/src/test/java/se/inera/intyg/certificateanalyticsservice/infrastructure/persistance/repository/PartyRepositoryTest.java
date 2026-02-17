package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.PartyEntity;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities;

@ExtendWith(MockitoExtension.class)
class PartyRepositoryTest {

  @InjectMocks
  private PartyRepository partyRepository;
  @Mock
  private PartyEntityRepository partyEntityRepository;

  @Test
  void shouldCreateNewRecipientEntityIfNotExists() {
    final var recipient = TestDataEntities.recipientPartyEntity();
    final var savedRecipient = mock(PartyEntity.class);
    when(partyEntityRepository.findByParty(recipient.getParty())).thenReturn(
        Optional.empty());
    when(partyEntityRepository.save(recipient)).thenReturn(savedRecipient);

    final var result = partyRepository.findOrCreate(recipient.getParty());

    assertEquals(savedRecipient, result);
  }

  @Test
  void shouldFindExistingRecipientEntity() {
    final var recipientName = TestDataEntities.recipientPartyEntity().getParty();
    final var entity = mock(PartyEntity.class);
    when(partyEntityRepository.findByParty(recipientName)).thenReturn(Optional.of(entity));

    final var result = partyRepository.findOrCreate(recipientName);

    assertEquals(entity, result);
  }

  @Test
  void shouldReturnNullIfRecipientIsNull() {
    assertNull(partyRepository.findOrCreate(null));
  }

  @Test
  void shouldReturnNullIfRecipientIsEmpty() {
    assertNull(partyRepository.findOrCreate(" "));
  }
}