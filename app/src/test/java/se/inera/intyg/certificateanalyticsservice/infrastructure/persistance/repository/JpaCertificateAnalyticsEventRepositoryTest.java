package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataPseudonymized.draftPseudonymizedMessageBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataPseudonymized.sentPseudonymizedMessageBuilder;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.EventEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper.EventMapper;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities;

@ExtendWith(MockitoExtension.class)
class JpaCertificateAnalyticsEventRepositoryTest {

  @Mock
  private EventEntityRepository eventEntityRepository;
  @Mock
  private EventMapper eventMapper;
  @InjectMocks
  private JpaAnalyticsEventRepository jpaCertificateAnalyticsEventRepository;

  @Test
  void shouldMapAndSaveCreatedEventMessage() {
    final var message = draftPseudonymizedMessageBuilder().build();
    final var entityToSave = mock(EventEntity.class);
    when(eventMapper.toEntity(message)).thenReturn(entityToSave);
    when(eventEntityRepository.save(entityToSave)).thenReturn(entityToSave);

    jpaCertificateAnalyticsEventRepository.save(message);

    verify(eventEntityRepository).save(entityToSave);
  }

  @Test
  void shouldReturnPseudonymizedMessageIfExists() {
    final var excepted = sentPseudonymizedMessageBuilder().build();

    final var entity = TestDataEntities.sentEventEntityBuilder().build();
    when(eventEntityRepository.findByMessageId(MESSAGE_ID)).thenReturn(Optional.of(entity));
    when(eventMapper.toDomain(entity)).thenReturn(excepted);

    final var actual = jpaCertificateAnalyticsEventRepository.findByMessageId(MESSAGE_ID);

    assertEquals(excepted, actual);
  }

  @Test
  void shouldNotAllowClear() {
    assertThrows(UnsupportedOperationException.class, () ->
        jpaCertificateAnalyticsEventRepository.clear()
    );
  }
}
