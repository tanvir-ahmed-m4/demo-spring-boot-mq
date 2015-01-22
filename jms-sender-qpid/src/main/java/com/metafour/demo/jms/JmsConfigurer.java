package com.metafour.demo.jms;

import javax.jms.ConnectionFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

@Configuration
public class JmsConfigurer {

  private static final Logger logger = Logger.getLogger(JmsConfigurer.class);
  
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

}
