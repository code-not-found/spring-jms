package com.codenotfound.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderReceiver {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(OrderReceiver.class);

  @Value("${destination.status1}")
  private String status1Destination;

  @Value("${destination.status2}")
  private String status2Destination;

  @Autowired
  JmsTemplate jmsTemplate;

  @JmsListener(destination = "${destination.order}",
      containerFactory = "orderDefaultJmsListenerContainerFactory")
  public void receive(String order) {
    LOGGER.info("received order='{}'", order);
    jmsTemplate.convertAndSend(status1Destination, "Accepted");
    jmsTemplate.convertAndSend(status2Destination, "Accepted");
  }
}
