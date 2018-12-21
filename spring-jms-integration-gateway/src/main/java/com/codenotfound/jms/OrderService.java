package com.codenotfound.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

public class OrderService {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(OrderService.class);

  public Message<?> order(Message<?> order) {
    LOGGER.info("received order='{}'",
        order.getHeaders().getReplyChannel());

    Message<?> status = MessageBuilder.withPayload("Accepted")
        .setHeader("jms_correlationId",
            order.getHeaders().get("jms_messageId"))
        .setReplyChannelName("inboundOrderResponseChannel").build();
    LOGGER.info("sending status='{}'", status);

    return status;
  }
}
