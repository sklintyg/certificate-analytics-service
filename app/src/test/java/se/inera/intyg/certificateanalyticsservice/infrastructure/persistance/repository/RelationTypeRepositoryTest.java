package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CERTIFICATE_PARENT_TYPE;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities.relationTypeEntityBuilder;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RelationTypeRepositoryTest {

  @Mock
  private RelationTypeEntityRepository relationTypeEntityRepository;
  @InjectMocks
  private RelationTypeRepository relationTypeRepository;

  @Test
  void shouldCreateNewRelationTypeEntityIfNotExists() {
    final var expected = relationTypeEntityBuilder()
        .key(99L)
        .build();

    final var relationTypeEntity = relationTypeEntityBuilder().build();

    when(relationTypeEntityRepository.findByRelationType(CERTIFICATE_PARENT_TYPE))
        .thenReturn(Optional.empty());
    when(relationTypeEntityRepository.save(relationTypeEntity)).thenReturn(expected);

    final var actual = relationTypeRepository.findOrCreate(CERTIFICATE_PARENT_TYPE);

    assertEquals(expected, actual);
  }

  @Test
  void shouldFindExistingRelationTypeEntity() {
    final var expected = relationTypeEntityBuilder()
        .key(99L)
        .build();

    when(relationTypeEntityRepository.findByRelationType(CERTIFICATE_PARENT_TYPE))
        .thenReturn(Optional.of(expected));

    final var actual = relationTypeRepository.findOrCreate(CERTIFICATE_PARENT_TYPE);

    assertEquals(expected, actual);
  }

  @Test
  void shouldReturnNullIfRelationTypeIsNull() {
    assertNull(relationTypeRepository.findOrCreate(null));
  }

  @Test
  void shouldReturnNullIfRelationTypeIsEmpty() {
    assertNull(relationTypeRepository.findOrCreate(" "));
  }
}