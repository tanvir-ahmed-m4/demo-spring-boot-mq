package com.metafour.demo.qpid.amqp_1_0;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public class QpidHelloSend {

	public QpidHelloSend() {
	}

	public static void main(String[] args) {
		QpidHelloSend producer = new QpidHelloSend();
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

			MessageProducer messageProducer = session.createProducer(destination);

			TextMessage message = session.createTextMessage("Hello world!");
			messageProducer.send(message);
			System.out.println("Message sent!");

			connection.close();
			context.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}