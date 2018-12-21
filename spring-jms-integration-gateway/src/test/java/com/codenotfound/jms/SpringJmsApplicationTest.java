package com.codenotfound.jms;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class SpringJmsApplicationTest {

  @Autowired
  private ApplicationContext applicationContext;

  @Test
  public void testIntegrationGateway() {
    MessageChannel outboundOrderRequestChannel =
        applicationContext.getBean("outboundOrderRequestChannel",
            MessageChannel.class);
    QueueChannel outboundOrderResponseChannel = applicationContext
        .getBean("outboundOrderResponseChannel", QueueChannel.class);

    outboundOrderRequestChannel
        .send(new GenericMessage<>("order-001"));

    assertThat(
        outboundOrderResponseChannel.receive(5000).getPayload())
            .isEqualTo("Accepted");;
  }
}
