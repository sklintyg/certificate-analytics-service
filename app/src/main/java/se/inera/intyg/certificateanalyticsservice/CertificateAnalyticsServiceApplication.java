package se.inera.intyg.certificateanalyticsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CertificateAnalyticsServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(CertificateAnalyticsServiceApplication.class, args);
  }
}
