package com.example.library.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${spring.rabbitmq.Exchange}")
    String exchange;
    @Value("${spring.rabbitmq.Queue}")
    String firstqueue;
    @Value("${spring.rabbitmq.SaveQueue}")
    String savequeue;
    @Value("${spring.rabbitmq.routingKey}")
    String routingKey;



    @Bean
    DirectExchange exchange(){return new DirectExchange(exchange);}
    @Bean
    Queue firstQueue(){return new Queue(firstqueue,false);}
    @Bean
    Queue saveQueue(){return new Queue(savequeue,false);}
    @Bean
    Binding binding(Queue firstQueue, DirectExchange exchange){
        return BindingBuilder.bind(firstQueue).to(exchange).with(routingKey);
    }
    @Bean
    Binding bindingsavequeue(Queue saveQueue, DirectExchange exchange){
        return BindingBuilder.bind(saveQueue).to(exchange).with(routingKey);
    }
    @Bean
    public MessageConverter jsonMessageConverter(){return new Jackson2JsonMessageConverter();}
}