package com.metafour.demo.qpid.amqp_0_9;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
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
    try {
      Properties properties = new Properties();
      properties.load(this.getClass().getResourceAsStream("/hello.properties"));
      Context context = new InitialContext(properties);

      ConnectionFactory connectionFactory =
          (ConnectionFactory) context.lookup("qpidConnectionfactory");
      Connection connection = connectionFactory.createConnection();
      connection.start();

      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      Destination destination = (Destination) context.lookup("topicExchange");

      MessageConsumer messageConsumer = session.createConsumer(destination);

      TextMessage message = (TextMessage) messageConsumer.receive();
      logger.info("Message received: " + message.getText());

      connection.close();
      context.close();
    } catch (Exception e) {
      logger.fatal(e.getMessage(), e);
    }
  }
}
