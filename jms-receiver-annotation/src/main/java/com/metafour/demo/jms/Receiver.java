package com.metafour.demo.jms;

import org.apache.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

  private static final Logger logger = Logger.getLogger(Receiver.class);

  /**
   * When you receive a message, print it out
   */
//  @JmsListener(destination = "${msg.queue}")
  public void receiveMessage(String message) {
    logger.info("Received message <" + message + ">");
  }
  
//  @JmsListener(destination = "${msg.queue}")
  public void receiveMessage(MyBean bean) {
    logger.info("Received bean <" + bean.getId() + ":" + bean.getName() + ">");
  }

//  @JmsListener(destination = "${msg.queue}")
  public void receiveMessage(byte[] data) {
    logger.info("Received byte array <" + new String(data) + ">");
  }
  
  @JmsListener(destination = "${msg.queue}")
  public void receiveMessage(Object obj) {
    logger.info("Received object <" + obj.toString() + ">");
    if (obj instanceof String) {
      receiveMessage((String) obj);
    } else if (obj instanceof MyBean) {
      receiveMessage((MyBean) obj);
    } else {
      logger.warn("Speachless!");
    }
  }
}
