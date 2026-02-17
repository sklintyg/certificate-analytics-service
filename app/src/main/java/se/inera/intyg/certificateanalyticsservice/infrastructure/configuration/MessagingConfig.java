package se.inera.intyg.certificateanalyticsservice.infrastructure.configuration;

import jakarta.jms.ConnectionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

@Configuration
@EnableJms
public class MessagingConfig {

  @Bean
  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
      ConnectionFactory connectionFactory,
      DefaultJmsListenerContainerFactoryConfigurer configurer) {
    final var defaultJmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
    configurer.configure(defaultJmsListenerContainerFactory, connectionFactory);
    defaultJmsListenerContainerFactory.setSessionTransacted(true);

    defaultJmsListenerContainerFactory.setErrorHandler(t ->
        LoggerFactory.getLogger("JMS.Listener")
            .info("Listener threw; broker will redeliver or DLQ. {}", t.toString())
    );

    return defaultJmsListenerContainerFactory;
  }
}
