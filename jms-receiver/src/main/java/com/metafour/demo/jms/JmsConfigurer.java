package com.metafour.demo.jms;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class JmsConfigurer {

  private static final Logger logger = Logger.getLogger(JmsConfigurer.class);
  
  @Value("${msg.queue}")
  private String msgQueue;
  
  @Value("${broker.url:tcp://localhost:61616}")
  private String brokerURL;

  @Bean
  public ConnectionFactory connectionFactory() {
    logger.info("brokerURL: " + brokerURL);
    ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
    cf.setBrokerURL(brokerURL);
    
    return cf;
  }
  
  @Bean
  Receiver receiver() {
      return new Receiver();
  }

  @Bean
  MessageListenerAdapter adapter(Receiver receiver) {
      MessageListenerAdapter messageListener
              = new MessageListenerAdapter(receiver);
      messageListener.setDefaultListenerMethod("receiveMessage");
      return messageListener;
  }

  @Bean
  SimpleMessageListenerContainer container(MessageListenerAdapter messageListener,
                                           ConnectionFactory connectionFactory) {
      SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
      container.setMessageListener(messageListener);
      container.setConnectionFactory(connectionFactory);
      container.setDestinationName(msgQueue);
      return container;
  }

}
