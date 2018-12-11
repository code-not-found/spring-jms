package com.codenotfound.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(Sender.class);

  @Autowired
  private JmsTemplate jmsTemplate;

  public void send(String destination, String message,
      boolean isHighPriority) {
    LOGGER.info("sending message='{}' with highPriority='{}'",
        message, isHighPriority);

    if (isHighPriority) {
      jmsTemplate.convertAndSend(destination, message, m -> {
        m.setStringProperty("priority", "high");
        return m;
      });
    } else {
      jmsTemplate.convertAndSend(destination, message, m -> {
        m.setStringProperty("priority", "low");
        return m;
      });
    }
  }
}
