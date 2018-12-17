package com.codenotfound.jms;

import javax.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.jms.ChannelPublishingJmsMessageListener;
import org.springframework.integration.jms.JmsMessageDrivenEndpoint;
import org.springframework.jms.listener.SimpleMessageListenerContainer;

@Configuration
public class ConsumingChannelConfig {

  @Value("${destination.integration}")
  private String integrationDestination;

  @Bean
  public DirectChannel consumingChannel() {
    return new DirectChannel();
  }

  @Bean
  public JmsMessageDrivenEndpoint pollableJmsChannel(
      ConnectionFactory connectionFactory) {
    JmsMessageDrivenEndpoint endpoint = new JmsMessageDrivenEndpoint(
        simpleMessageListenerContainer(connectionFactory),
        channelPublishingJmsMessageListener());
    endpoint.setOutputChannel(consumingChannel());

    return endpoint;
  }

  @Bean
  public SimpleMessageListenerContainer simpleMessageListenerContainer(
      ConnectionFactory connectionFactory) {
    SimpleMessageListenerContainer container =
        new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setDestinationName(integrationDestination);
    return container;
  }

  @Bean
  public ChannelPublishingJmsMessageListener channelPublishingJmsMessageListener() {
    return new ChannelPublishingJmsMessageListener();
  }

  @Bean
  @ServiceActivator(inputChannel = "consumingChannel")
  public CountDownLatchHandler countDownLatchHandler() {
    return new CountDownLatchHandler();
  }
}
