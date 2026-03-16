package se.inera.intyg.certificateanalyticsservice.config.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public record AppProperties(
    @NotNull @Valid Pseudonymization pseudonymization,
    @NotNull @Valid Jms jms
) {

  public record Pseudonymization(
      @NotBlank String key,
      @NotBlank String context
  ) {}

  public record Jms(
      @NotBlank String queueName
  ) {}
}
