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

  @JmsListener(destination = "${queue.boot}",
      selector = "priority = 'high'")
  public void receiveHigh(String message) {
    LOGGER.info("received high priority message='{}'", message);
    latch.countDown();
  }

  @JmsListener(destination = "${queue.boot}",
      selector = "priority = 'low'")
  public void receiveLow(String message) {
    LOGGER.info("received low priority message='{}'", message);
    latch.countDown();
  }
}
