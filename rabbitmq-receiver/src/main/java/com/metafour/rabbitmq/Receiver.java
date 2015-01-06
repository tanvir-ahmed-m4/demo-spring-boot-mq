package com.metafour.rabbitmq;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

public class Receiver {

	private static final Logger logger = Logger.getLogger(Receiver.class);
	
	private CountDownLatch latch = new CountDownLatch(1);

	public void receiveMessage(String message) {
		logger.info("Received <" + message + ">");
		latch.countDown();
	}

	public void receiveMessage(byte[] payload) {
		logger.info("Payload <" + new String(payload) + ">");
		latch.countDown();
	}
	
	public CountDownLatch getLatch() {
		return latch;
	}
	
}
