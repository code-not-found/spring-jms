package com.codenotfound;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.concurrent.TimeUnit;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import com.codenotfound.jms.Sender;
import com.codenotfound.jms.StatusMessageListener;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class SpringJmsApplicationTest {

  @ClassRule
  public static EmbeddedActiveMQBroker broker =
      new EmbeddedActiveMQBroker();

  @Autowired
  private Sender sender;

  @Autowired
  private DefaultMessageListenerContainer dmlc;

  @Autowired
  private SimpleMessageListenerContainer smlc;

  @Test
  public void testReceive() throws Exception {
    sender.send("order-002");

    StatusMessageListener status1MessageListener =
        (StatusMessageListener) dmlc.getMessageListener();
    status1MessageListener.getLatch().await(10000,
        TimeUnit.MILLISECONDS);
    assertThat(status1MessageListener.getLatch().getCount())
        .isEqualTo(0);

    StatusMessageListener status2MessageListener =
        (StatusMessageListener) smlc.getMessageListener();
    status2MessageListener.getLatch().await(10000,
        TimeUnit.MILLISECONDS);
    assertThat(status2MessageListener.getLatch().getCount())
        .isEqualTo(0);
  }
}
