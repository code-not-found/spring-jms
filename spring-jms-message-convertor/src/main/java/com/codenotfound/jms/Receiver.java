package com.codenotfound.jms;

import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(Receiver.class);

  private CountDownLatch latch = new CountDownLatch(1);

  public CountDownLatch getLatch() {
    return latch;
  }

  @JmsListener(destination = "${queue.boot}")
  public void receive(Person person,
      @Header(JmsHeaders.CORRELATION_ID) String correlationId,
      MessageHeaders messageHeaders) {
    LOGGER.info("received person='{}'", person);
    LOGGER.info("JMSCorrelationID='{}'", correlationId);
    LOGGER.info("AccountID='{}'", messageHeaders.get("AccountID"));

    latch.countDown();
  }
}
