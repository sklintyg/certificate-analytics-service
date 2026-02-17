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
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.CareProviderEntity;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities;

@ExtendWith(MockitoExtension.class)
class CareProviderRepositoryTest {

  @InjectMocks
  private CareProviderRepository careProviderRepository;
  @Mock
  private CareProviderEntityRepository careProviderEntityRepository;

  @Test
  void shouldCreateNewCareProviderEntityIfNotExists() {
    final var careProvider = TestDataEntities.careProviderEntity();
    final var savedCareProvider = mock(CareProviderEntity.class);
    when(careProviderEntityRepository.findByHsaId(careProvider.getHsaId())).thenReturn(
        Optional.empty());
    when(careProviderEntityRepository.save(careProvider)).thenReturn(savedCareProvider);

    final var result = careProviderRepository.findOrCreate(careProvider.getHsaId());

    assertEquals(savedCareProvider, result);
  }

  @Test
  void shouldFindExistingCareProviderEntity() {
    final var hsaId = TestDataEntities.careProviderEntity().getHsaId();
    final var entity = mock(CareProviderEntity.class);
    when(careProviderEntityRepository.findByHsaId(hsaId)).thenReturn(Optional.of(entity));

    final var result = careProviderRepository.findOrCreate(hsaId);

    assertEquals(entity, result);
  }

  @Test
  void shouldReturnNullIfHsaIdIsNull() {
    assertNull(careProviderRepository.findOrCreate(null));
  }

  @Test
  void shouldReturnNullIfHsaIdIsEmpty() {
    assertNull(careProviderRepository.findOrCreate(" "));
  }
}

