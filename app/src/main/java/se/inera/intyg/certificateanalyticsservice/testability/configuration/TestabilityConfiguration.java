package se.inera.intyg.certificateanalyticsservice.testability.configuration;

import static se.inera.intyg.certificateanalyticsservice.testability.configuration.TestabilityConfiguration.TESTABILITY_PROFILE;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile(TESTABILITY_PROFILE)
public class TestabilityConfiguration {

  public static final String TESTABILITY_PROFILE = "testability";

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf ->
            csrf.ignoringRequestMatchers("/testability/**")
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/testability/**").permitAll()
            .requestMatchers("/actuator/health/**").permitAll()
            .anyRequest().denyAll()
        );
    return http.build();
  }
}