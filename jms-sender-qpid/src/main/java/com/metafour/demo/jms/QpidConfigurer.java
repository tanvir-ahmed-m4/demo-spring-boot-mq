package com.metafour.demo.jms;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;

@Configuration
public class QpidConfigurer {

  private static final Logger logger = Logger.getLogger(QpidConfigurer.class);
  
//  @Value("${qpid.properties:qpid.properties}")
//  private String qpidPropertiesFile;
  
  @Bean
  public PropertiesFactoryBean qpidProperties() {
    PropertiesFactoryBean pfb = new PropertiesFactoryBean();
    pfb.setLocation(new ClassPathResource("qpid.properties"));
    return pfb;
  }
  
  @Resource(name="qpidProperties")
  private java.util.Properties properties;

  @Bean
  public JndiTemplate jndiTemplate() {
      JndiTemplate jndiTemplate = new JndiTemplate();
      jndiTemplate.setEnvironment(properties);
      return jndiTemplate;
  }

  @Bean
  public JndiObjectFactoryBean qpidConnectionFactory() {
    JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
    jndiObjectFactoryBean.setJndiTemplate(jndiTemplate());
    jndiObjectFactoryBean.setJndiName("localhost");

    return jndiObjectFactoryBean;
  }

}
