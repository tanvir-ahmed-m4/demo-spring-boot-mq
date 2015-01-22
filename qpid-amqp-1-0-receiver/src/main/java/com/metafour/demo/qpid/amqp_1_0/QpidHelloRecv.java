package com.metafour.demo.qpid.amqp_1_0;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class QpidHelloRecv {

  private static final Logger logger = Logger.getLogger(QpidHelloRecv.class);

  public QpidHelloRecv() {}

  public static void main(String[] args) {
    QpidHelloRecv producer = new QpidHelloRecv();
    producer.runTest();
  }

  private void runTest() {
    try (InputStream propertiesStream = getClass().getResourceAsStream("/hello.properties")) {
      Properties properties = new Properties();
      // Read the hello.properties JNDI properties file and use contents to create the
      // InitialContext.
      properties.load(propertiesStream);
      Context context = new InitialContext(properties);
      // Alternatively, JNDI information can be supplied by setting the
      // "java.naming.factory.initial"
      // system property to value
      // "org.apache.qpid.amqp_1_0.jms.jndi.PropertiesFileInitialContextFactory"
      // and setting the "java.naming.provider.url" system property as a URL to a properties file.

      ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("localhost");
      Connection connection = connectionFactory.createConnection();

      Queue queue = (Queue) context.lookup("queue");

      Session consumerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      MessageConsumer messageConsumer = consumerSession.createConsumer(queue);

      final CountDownLatch latch = new CountDownLatch(1);
      messageConsumer.setMessageListener(new MessageListener() {
        public void onMessage(final Message message) {
          try {
            if (message instanceof TextMessage) {
              logger.info("Received message: " + ((TextMessage) message).getText());
            } else {
              logger.info("Received enexpected message type: " + message.getClass().getName());
            }

            latch.countDown();
          } catch (JMSException e) {
            logger.info("Caught exception in onMessage(): " + e.getMessage());
          }
        }
      });

      connection.start();

      int delay = 5;
      if (!latch.await(delay, TimeUnit.SECONDS)) {
        logger.info("Waited " + delay + " sec but no message recieved.");
      }

      connection.close();
      context.close();
    } catch (Exception exp) {
      logger.fatal("Caught exception: " + exp.getMessage(), exp);
    }
  }
}
