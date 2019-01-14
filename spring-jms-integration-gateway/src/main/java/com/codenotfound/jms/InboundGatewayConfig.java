package com.codenotfound.jms;

import javax.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.jms.ChannelPublishingJmsMessageListener;
import org.springframework.integration.jms.JmsInboundGateway;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
public class InboundGatewayConfig {

  @Value("${destination.order.request}")
  private String orderRequestDestination;

  @Bean
  public MessageChannel inboundOrderRequestChannel() {
    return new DirectChannel();
  }

  @Bean
  public MessageChannel inboundOrderResponseChannel() {
    return new DirectChannel();
  }

  @Bean
  @ServiceActivator(inputChannel = "inboundOrderRequestChannel")
  public OrderService orderService() {
    return new OrderService();
  }

  @Bean
  public JmsInboundGateway jmsInboundGateway(
      ConnectionFactory connectionFactory) {
    JmsInboundGateway gateway = new JmsInboundGateway(
        simpleMessageListenerContainer(connectionFactory),
        channelPublishingJmsMessageListener());
    gateway.setRequestChannel(inboundOrderRequestChannel());

    return gateway;
  }

  @Bean
  public SimpleMessageListenerContainer simpleMessageListenerContainer(
      ConnectionFactory connectionFactory) {
    SimpleMessageListenerContainer container =
        new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setDestinationName(orderRequestDestination);
    return container;
  }

  @Bean
  public ChannelPublishingJmsMessageListener channelPublishingJmsMessageListener() {
    ChannelPublishingJmsMessageListener channelPublishingJmsMessageListener =
        new ChannelPublishingJmsMessageListener();
    channelPublishingJmsMessageListener.setExpectReply(true);

    return channelPublishingJmsMessageListener;
  }
}
