package com.codenotfound.jms;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.AfterClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.codenotfound.jms.consumer.Receiver;
import com.codenotfound.jms.producer.Sender;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringJmsApplicationTest {

  private static ApplicationContext applicationContext;

  @Autowired
  void setContext(ApplicationContext applicationContext) {
    SpringJmsApplicationTest.applicationContext = applicationContext;
  }

  @AfterClass
  public static void afterClass() {
    ((ConfigurableApplicationContext) applicationContext).close();
  }

  @ClassRule
  public static EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();

  @Autowired
  private Sender sender;

  @Autowired
  private Receiver receiver;

  @Test
  public void testReceive() throws Exception {
    sender.send("helloworld.q", "Hello Spring JMS ActiveMQ!");

    receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    assertThat(receiver.getLatch().getCount()).isEqualTo(0);
  }
}
