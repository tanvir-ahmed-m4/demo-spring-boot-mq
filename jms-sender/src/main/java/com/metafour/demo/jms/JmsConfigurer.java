package com.metafour.demo.jms;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JmsConfigurer {

  private static final Logger logger = Logger.getLogger(JmsConfigurer.class);
  
  @Value("${broker.url:tcp://localhost:61616}")
  private String brokerURL;

  @Value("${max.connections:10}")
  private int maxConnections;

  @Value("${maximum.active.session.per.connection:10}")
  private int maximumActiveSessionPerConnection;

  @Bean
  public ConnectionFactory pooledConnectionFactory() {
    logger.info("brokerURL: " + brokerURL);
    logger.info("maxConnections: " + maxConnections);
    ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
    cf.setBrokerURL(brokerURL);
    
    PooledConnectionFactory pcf = new PooledConnectionFactory();
    pcf.setConnectionFactory(cf);
    pcf.setMaxConnections(maxConnections);
    pcf.setMaximumActiveSessionPerConnection(maximumActiveSessionPerConnection);
    
    return pcf;
  }  
}
