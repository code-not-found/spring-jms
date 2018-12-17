package com.codenotfound.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.SimpleMessageListenerContainer;

@Configuration
@EnableJms
public class ReceiverConfig {

  @Value("${activemq.broker-url}")
  private String brokerUrl;

  @Value("${destination.status1}")
  private String status1Destination;

  @Value("${destination.status2}")
  private String status2Destination;

  @Bean
  public ActiveMQConnectionFactory receiverActiveMQConnectionFactory() {
    ActiveMQConnectionFactory activeMQConnectionFactory =
        new ActiveMQConnectionFactory();
    activeMQConnectionFactory.setBrokerURL(brokerUrl);

    return activeMQConnectionFactory;
  }

  @Bean
  public DefaultJmsListenerContainerFactory orderDefaultJmsListenerContainerFactory() {
    DefaultJmsListenerContainerFactory factory =
        new DefaultJmsListenerContainerFactory();
    factory
        .setConnectionFactory(receiverActiveMQConnectionFactory());
    factory.setConcurrency("3-10");

    return factory;
  }

  @Bean
  public SimpleJmsListenerContainerFactory orderSimpleJmsListenerContainerFactory() {
    SimpleJmsListenerContainerFactory factory =
        new SimpleJmsListenerContainerFactory();
    factory
        .setConnectionFactory(receiverActiveMQConnectionFactory());

    return factory;
  }

  @Bean
  public DefaultMessageListenerContainer orderMessageListenerContainer() {
    SimpleJmsListenerEndpoint endpoint =
        new SimpleJmsListenerEndpoint();
    endpoint.setMessageListener(new StatusMessageListener("DMLC"));
    endpoint.setDestination(status1Destination);

    return orderDefaultJmsListenerContainerFactory()
        .createListenerContainer(endpoint);
  }

  @Bean
  public SimpleMessageListenerContainer statusMessageListenerContainer() {
    SimpleJmsListenerEndpoint endpoint =
        new SimpleJmsListenerEndpoint();
    endpoint.setMessageListener(new StatusMessageListener("SMLC"));
    endpoint.setDestination(status2Destination);

    return orderSimpleJmsListenerContainerFactory()
        .createListenerContainer(endpoint);
  }
}
