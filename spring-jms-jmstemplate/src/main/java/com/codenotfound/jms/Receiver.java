package com.codenotfound.jms;

import javax.jms.Destination;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

  @Autowired
  private Destination statusDestination;

  @Autowired
  private JmsTemplate jmsTemplate;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(Receiver.class);

  @JmsListener(destination = "${destination.order}")
  public void receiveOrder(String orderNumber,
      @Header(JmsHeaders.MESSAGE_ID) String messageId) {
    LOGGER.info("received OrderNumber='{}' with MessageId='{}'",
        orderNumber, messageId);

    LOGGER.info("sending Status='Accepted' with CorrelationId='{}'",
        messageId);

    jmsTemplate.send(statusDestination, messageCreator -> {
      TextMessage message =
          messageCreator.createTextMessage("Accepted");
      message.setJMSCorrelationID(messageId);
      return message;
    });
  }
}
