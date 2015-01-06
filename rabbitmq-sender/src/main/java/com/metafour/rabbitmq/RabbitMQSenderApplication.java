package com.metafour.rabbitmq;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class RabbitMQSenderApplication implements CommandLineRunner {

	private static final Logger logger = Logger.getLogger(RabbitMQSenderApplication.class);
	
	final static String queueName = "spring-boot";

	@Autowired
	AnnotationConfigApplicationContext context;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("spring-boot-exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(RabbitMQSenderApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Waiting five seconds...");
        Thread.sleep(5000);
        logger.info("Sending message...");
        rabbitTemplate.convertAndSend(queueName, "Hello from RabbitMQ!");
        Thread.sleep(5000);
        context.close();
    }
    
    @EnableScheduling
    static class ScheduledProducer {

        @Autowired
        private volatile RabbitTemplate rabbitTemplate;

        private final AtomicInteger counter = new AtomicInteger();

        @Scheduled(fixedRate = 1000)
        public void sendMessage() {
        	logger.info("Sending scheduled message...");
            rabbitTemplate.convertAndSend(queueName, "Hello World " + counter.incrementAndGet());
        }
    }
}
