package com.metafour.demo.jms;

import javax.jms.ConnectionFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

@Configuration
public class JmsConfigurer {

  private static final Logger logger = Logger.getLogger(JmsConfigurer.class);

  @Value("${msg.queue}")
  private String msgQueue;
  
  @Value("${session.cache.size:5}")
  private int sessionCacheSize;

  @Value("#{qpidConnectionFactory}")
  private ConnectionFactory qpidConnectionFactory;

  @Bean
  public CachingConnectionFactory connectionFactory() {
    CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
    cachingConnectionFactory.setTargetConnectionFactory(qpidConnectionFactory);
    cachingConnectionFactory.setSessionCacheSize(sessionCacheSize);

    return cachingConnectionFactory;
  }

  @Bean
  Receiver receiver() {
    return new Receiver();
  }

  @Bean
  MessageListenerAdapter adapter(Receiver receiver) {
    MessageListenerAdapter messageListener = new MessageListenerAdapter(receiver);
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
