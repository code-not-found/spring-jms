package com.codenotfound.jms;

import javax.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.jms.JmsOutboundGateway;
import org.springframework.messaging.MessageChannel;

@Configuration
public class OutboundGatewayConfig {

  @Value("${destination.order.request}")
  private String orderRequestDestination;

  @Value("${destination.order.response}")
  private String orderResponseDestination;

  @Bean
  public MessageChannel outboundOrderRequestChannel() {
    return new DirectChannel();
  }

  @Bean
  public MessageChannel outboundOrderResponseChannel() {
    return new QueueChannel();
  }

  @Bean
  @ServiceActivator(inputChannel = "outboundOrderRequestChannel")
  public JmsOutboundGateway jmsOutboundGateway(
      ConnectionFactory connectionFactory) {
    JmsOutboundGateway gateway = new JmsOutboundGateway();
    gateway.setConnectionFactory(connectionFactory);
    gateway.setRequestDestinationName(orderRequestDestination);
    gateway.setReplyDestinationName(orderResponseDestination);
    gateway.setReplyChannel(outboundOrderResponseChannel());

    return gateway;
  }
}
