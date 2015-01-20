package com.metafour.demo.jms;

import org.apache.log4j.Logger;


public class Receiver {

  private static final Logger logger = Logger.getLogger(Receiver.class);

  /**
   * When you receive a message, print it out
   */
  public void receiveMessage(String message) {
    logger.info("Received message <" + message + ">");
  }
  
  public void receiveMessage(MyBean bean) {
    logger.info("Received bean <" + bean.getId() + ":" + bean.getName() + ">");
  }
}
