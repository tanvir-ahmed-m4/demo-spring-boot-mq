package com.metafour.demo.qpid.amqp_1_0;

import java.io.InputStream;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class QpidHelloSend {

  private static final Logger logger = Logger.getLogger(QpidHelloSend.class);

  public QpidHelloSend() {}

  public static void main(String[] args) {
    QpidHelloSend producer = new QpidHelloSend();
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

      Session producersession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      Queue queue = (Queue) context.lookup("queue");
//      Destination destination = (Destination) context.lookup("topicExchange");


      connection.start();

      MessageProducer queueProducer = producersession.createProducer(queue);
//      MessageProducer destinationProducer = producersession.createProducer(destination);

      TextMessage message = producersession.createTextMessage("Hello AMQP 1.0 world at " + System.currentTimeMillis() + "!");

      queueProducer.send(message);
//      destinationProducer.send(message);
      logger.info("Message sent!");

      connection.close();
      context.close();
    } catch (Exception exp) {
      logger.fatal("Caught exception: " + exp.getMessage(), exp);
    }
  }
}
