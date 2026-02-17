package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_MESSAGE_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities.messageEntity;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataPseudonymized.messagePseudonymizedMessageBuilder;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.MessageEntityRepository;

@ExtendWith(MockitoExtension.class)
class MessageEntityMapperTest {

  @Mock
  private MessageEntityRepository messageEntityRepository;
  @InjectMocks
  private MessageEntityMapper messageEntityMapper;

  @Test
  void shallReturnNullIfMessageIdNull() {
    final var actual = messageEntityMapper.map(messagePseudonymizedMessageBuilder()
        .messageId(null)
        .build()
    );
    assertNull(actual, "Expected null when no message id");
  }

  @Test
  void shallReturnNullIfMessageIdEmpty() {
    final var actual = messageEntityMapper.map(messagePseudonymizedMessageBuilder()
        .messageId("")
        .build()
    );
    assertNull(actual, "Expected null when no message id");
  }

  @Test
  void shallReturnExistingMessageIfAlreadyExists() {
    final var expected = messageEntity().build();

    when(messageEntityRepository.findByMessageId(HASHED_MESSAGE_ID))
        .thenReturn(Optional.of(expected));

    final var actual = messageEntityMapper.map(messagePseudonymizedMessageBuilder().build());
    assertEquals(expected, actual);
  }

  @Test
  void shallReturnNewMessageIfNotExists() {
    final var expected = messageEntity().build();

    when(messageEntityRepository.findByMessageId(HASHED_MESSAGE_ID))
        .thenReturn(Optional.empty());
    when(messageEntityRepository.save(expected)).thenReturn(expected);

    final var actual = messageEntityMapper.map(messagePseudonymizedMessageBuilder().build());

    assertEquals(expected, actual);
  }
}