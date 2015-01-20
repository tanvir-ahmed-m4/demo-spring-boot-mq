package com.metafour.demo.jms;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableJms
@EnableScheduling
public class JmsSenderApplication  implements CommandLineRunner {

  private static final Logger logger = Logger.getLogger(JmsSenderApplication.class);
  
  @Autowired
  private AnnotationConfigApplicationContext context;
  
  @Autowired
  private JmsTemplate jmsTemplate;

  @Value("${msg.queue:inq}")
  private String msgQueue;
  
  public static void main(String[] args) {
    SpringApplication.run(JmsSenderApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    logger.info("Scheduler should start now...");
    logger.info("Waiting few seconds...");
    Thread.sleep(5000);
    context.close();
  }
  
  private final AtomicInteger counter = new AtomicInteger();

  @Scheduled(fixedRate = 1000)
  public void scheduledMessage() {
      
      // Send a message
      MessageCreator messageCreator = new MessageCreator() {
          @Override
          public Message createMessage(Session session) throws JMSException {
              return session.createTextMessage("[" + counter.incrementAndGet() + "] ping!");
          }
      };
      logger.info("Sending scheduled message... "  + counter.get());
      jmsTemplate.send(msgQueue, messageCreator);
  }
  
  @Scheduled(fixedRate = 500)
  public void scheduledBean() {
      
      logger.info("Sending scheduled bean... "  + counter.get());
      MyBean mybean = new MyBean();
      mybean.setId(System.currentTimeMillis());
      mybean.setName(System.getProperty("user.name"));
//      MessageConverter messageConverter = new SimpleMessageConverter();
//      jmsTemplate.setMessageConverter(messageConverter);
      jmsTemplate.convertAndSend(msgQueue, mybean);
  }
  
  @PostConstruct
  public void init() {
    logger.info("msgQueue: " + msgQueue);
  }
}
