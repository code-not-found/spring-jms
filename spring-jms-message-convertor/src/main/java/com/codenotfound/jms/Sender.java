package com.codenotfound.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

@Component
public class Sender {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(Sender.class);

  @Autowired
  private JmsTemplate jmsTemplate;

  public void send(String destination, Person person) {
    LOGGER.info("sending person='{}' to destination='{}'", person,
        destination);

    jmsTemplate.convertAndSend(destination, person,
        new MessagePostProcessor() {
          public Message postProcessMessage(Message message)
              throws JMSException {
            message.setJMSCorrelationID("123-00001");
            message.setIntProperty("AccountID", 1234);

            return message;
          }
        });
  }
}
