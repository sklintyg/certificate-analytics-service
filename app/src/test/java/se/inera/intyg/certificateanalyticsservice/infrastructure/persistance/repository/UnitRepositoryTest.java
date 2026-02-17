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
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.UnitEntity;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities;

@ExtendWith(MockitoExtension.class)
class UnitRepositoryTest {

  @InjectMocks
  private UnitRepository unitRepository;
  @Mock
  private UnitEntityRepository unitEntityRepository;

  @Test
  void shouldCreateNewUnitEntityIfNotExists() {
    final var unit = TestDataEntities.unitEntity();
    final var savedUnit = mock(UnitEntity.class);
    when(unitEntityRepository.findByHsaId(unit.getHsaId())).thenReturn(Optional.empty());
    when(unitEntityRepository.save(unit)).thenReturn(savedUnit);

    final var result = unitRepository.findOrCreate(unit.getHsaId());

    assertEquals(savedUnit, result);
  }

  @Test
  void shouldFindExistingUnitEntity() {
    final var hsaId = TestDataEntities.unitEntity().getHsaId();
    final var entity = mock(UnitEntity.class);
    when(unitEntityRepository.findByHsaId(hsaId)).thenReturn(Optional.of(entity));

    final var result = unitRepository.findOrCreate(hsaId);

    assertEquals(entity, result);
  }

  @Test
  void shouldReturnNullIfUnitIsNull() {
    assertNull(unitRepository.findOrCreate(null));
  }

  @Test
  void shouldReturnNullIfUnitIsEmpty() {
    assertNull(unitRepository.findOrCreate(" "));
  }
}

