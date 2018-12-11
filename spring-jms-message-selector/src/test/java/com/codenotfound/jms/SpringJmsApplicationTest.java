package com.codenotfound.jms;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class SpringJmsApplicationTest {

  @Autowired
  private Sender sender;

  @Autowired
  private Receiver receiver;

  @Test
  public void testReceive() throws Exception {
    sender.send("selector.q", "High priority!", true);
    sender.send("selector.q", "Low priority!", false);

    receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    assertThat(receiver.getLatch().getCount()).isEqualTo(0);
  }
}
