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
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.OriginEntity;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities;

@ExtendWith(MockitoExtension.class)
class OriginRepositoryTest {

  @InjectMocks
  private OriginRepository originRepository;
  @Mock
  private OriginEntityRepository originEntityRepository;

  @Test
  void shouldCreateNewOriginEntityIfNotExists() {
    final var origin = TestDataEntities.originEntity();
    final var savedOrigin = mock(OriginEntity.class);
    when(originEntityRepository.findByOrigin(origin.getOrigin())).thenReturn(Optional.empty());
    when(originEntityRepository.save(origin)).thenReturn(savedOrigin);

    final var result = originRepository.findOrCreate(origin.getOrigin());

    assertEquals(savedOrigin, result);
  }

  @Test
  void shouldFindExistingOriginEntity() {
    final var origin = TestDataEntities.originEntity().getOrigin();
    final var entity = mock(OriginEntity.class);
    when(originEntityRepository.findByOrigin(origin)).thenReturn(Optional.of(entity));

    final var result = originRepository.findOrCreate(origin);

    assertEquals(entity, result);
  }

  @Test
  void shouldReturnNullIfOriginIsNull() {
    assertNull(originRepository.findOrCreate(null));
  }

  @Test
  void shouldReturnNullIfOriginIsEmpty() {
    assertNull(originRepository.findOrCreate(" "));
  }
}

