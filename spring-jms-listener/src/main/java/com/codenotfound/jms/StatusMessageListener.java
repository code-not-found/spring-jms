package com.codenotfound.jms;

import java.util.concurrent.CountDownLatch;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusMessageListener implements MessageListener {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(Sender.class);

  private String id;

  private CountDownLatch latch = new CountDownLatch(1);

  public StatusMessageListener(String id) {
    super();
    this.id = id;
  }

  @Override
  public void onMessage(Message message) {
    if (message instanceof TextMessage) {
      try {
        String text = ((TextMessage) message).getText();
        LOGGER.info("id='{}' received status='{}'", id, text);
        latch.countDown();
      } catch (JMSException e) {
        LOGGER.error("unable to read message payload", e);
      }
    } else {
      LOGGER.error("received unsupported message type");
    }
  }

  public CountDownLatch getLatch() {
    return latch;
  }
}
