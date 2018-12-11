package com.codenotfound.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class CustomMessageConverter {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(CustomMessageConverter.class);

  @Bean
  public MessageConverter jacksonJmsMessageConverter() {
    MappingJackson2MessageConverter converter =
        new MappingJackson2MessageConverter() {

          @Override
          public Message toMessage(Object object, Session session)
              throws JMSException {
            TextMessage message =
                (TextMessage) super.toMessage(object, session);
            LOGGER.info("outbound json='{}'", message.getText());

            return message;
          }

          @Override
          public Object fromMessage(Message message)
              throws JMSException {
            LOGGER.info("inbound json='{}'",
                ((TextMessage) message).getText());

            return super.fromMessage(message);
          }
        };

    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");

    return converter;
  }
}
