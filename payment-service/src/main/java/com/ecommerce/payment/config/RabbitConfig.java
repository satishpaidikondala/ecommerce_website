package com.ecommerce.payment.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "ecommerce.exchange";
    public static final String ORDER_CREATED_QUEUE = "payment.order.created.queue";
    public static final String ORDER_CREATED_KEY = "order.created";

    @Bean public TopicExchange exchange() { return new TopicExchange(EXCHANGE); }
    @Bean public Queue orderCreatedQueue() { return new Queue(ORDER_CREATED_QUEUE, true); }
    @Bean public Binding orderCreatedBinding(Queue orderCreatedQueue, TopicExchange exchange) {
        return BindingBuilder.bind(orderCreatedQueue).to(exchange).with(ORDER_CREATED_KEY);
    }
    @Bean public Jackson2JsonMessageConverter messageConverter() { return new Jackson2JsonMessageConverter(); }
}
