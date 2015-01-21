package com.metafour.demo.qpid.amqp_0_9;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public class QpidHelloRecv {

	public QpidHelloRecv() {
	}

	public static void main(String[] args) {
		QpidHelloRecv producer = new QpidHelloRecv();
		producer.runTest();
	}

	private void runTest() {
		try {
			Properties properties = new Properties();
			properties.load(this.getClass().getResourceAsStream("/hello.properties"));
			Context context = new InitialContext(properties);

			ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("qpidConnectionfactory");
			Connection connection = connectionFactory.createConnection();
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = (Destination) context.lookup("topicExchange");

			MessageConsumer messageConsumer = session.createConsumer(destination);

			TextMessage message = (TextMessage) messageConsumer.receive();
			System.out.println("Message received: " + message.getText());

			connection.close();
			context.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}