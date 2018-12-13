package com.codenotfound;

import static org.assertj.core.api.Assertions.assertThat;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import com.codenotfound.jms.Sender;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class SpringJmsApplicationTest {

  @ClassRule
  public static EmbeddedActiveMQBroker broker =
      new EmbeddedActiveMQBroker();

  @Autowired
  private Sender sender;

  @Test
  public void testReceive() throws Exception {
    String correlationId = sender.sendOrder("order-001");
    String status = sender.receiveOrderStatus(correlationId);

    assertThat(status).isEqualTo("Accepted");
  }
}
