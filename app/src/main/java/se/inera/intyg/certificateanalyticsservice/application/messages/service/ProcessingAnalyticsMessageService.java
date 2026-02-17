package se.inera.intyg.certificateanalyticsservice.application.messages.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateanalyticsservice.application.messages.repository.AnalyticsMessageRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessingAnalyticsMessageService implements AnalyticsMessageService {

  private final AnalyticsMessageParserProvider analyticsMessageParserProvider;
  private final AnalyticMessagePseudonymizerProvider analyticMessagePseudonymizerProvider;
  private final AnalyticsMessageRepository analyticsMessageRepository;

  @Override
  public void process(String body, String type, String schemaVersion) {
    final var message = analyticsMessageParserProvider.parser(type, schemaVersion).parse(body);
    final var pseudonymizedMessage = analyticMessagePseudonymizerProvider.pseudonymizer(message)
        .pseudonymize(message);
    analyticsMessageRepository.save(pseudonymizedMessage);
    log.info("Processed, pseudonymized and stored message with id '{}'", message.getMessageId());
  }
}