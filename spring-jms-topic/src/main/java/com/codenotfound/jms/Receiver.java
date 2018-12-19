package com.codenotfound.jms;

import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(Receiver.class);

  private CountDownLatch latch = new CountDownLatch(2);

  public CountDownLatch getLatch() {
    return latch;
  }

  @JmsListener(destination = "${destination.topic}")
  public void receive1(String message) {
    LOGGER.info("'subscriber1' received message='{}'", message);
    latch.countDown();
  }

  @JmsListener(destination = "${destination.topic}")
  public void receive2(String message) {
    LOGGER.info("'subscriber2' received message='{}'", message);
    latch.countDown();
  }
}
