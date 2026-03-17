/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.certificateanalyticsservice.integrationtest.util;

import org.testcontainers.activemq.ActiveMQContainer;

public class Containers {

  public static ActiveMQContainer amqContainer;

  public static void ensureRunning() {
    amqContainer();
  }

  private static void amqContainer() {
    if (amqContainer == null) {
      amqContainer =
          new ActiveMQContainer("apache/activemq-classic:5.18.3")
              .withUser("activemqUser")
              .withPassword("activemqPassword");
    }

    if (!amqContainer.isRunning()) {
      amqContainer.start();
    }

    System.setProperty("spring.activemq.user", amqContainer.getUser());
    System.setProperty("spring.activemq.password", amqContainer.getPassword());
    System.setProperty(
        "spring.activemq.broker-url", withRedeliveryPolicy(amqContainer.getBrokerUrl()));
  }

  private static String withRedeliveryPolicy(String brokerUrl) {
    return brokerUrl
        + "?jms.redeliveryPolicy.maximumRedeliveries=3"
        + "&jms.redeliveryPolicy.initialRedeliveryDelay=100"
        + "&jms.redeliveryPolicy.useExponentialBackOff=true"
        + "&jms.redeliveryPolicy.backOffMultiplier=2"
        + "&jms.redeliveryPolicy.maximumRedeliveryDelay=2000";
  }
}
