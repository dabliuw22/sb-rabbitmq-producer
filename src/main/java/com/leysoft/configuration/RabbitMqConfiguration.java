
package com.leysoft.configuration;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    @Value(
            value = "${spring.rabbitmq.host}")
    private String rabbitHost;

    @Value(
            value = "${spring.rabbitmq.port}")
    private int rabbitPort;

    @Value(
            value = "${spring.rabbitmq.username}")
    private String rabbitUsername;

    @Value(
            value = "${spring.rabbitmq.password}")
    private String rabbitPassword;

    @Value(
            value = "${rabbitmq.queue.name}")
    private String queueName;

    @Value(
            value = "${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value(
            value = "${rabbitmq.exchange.durable:true}")
    private boolean exchangeDurable;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitHost);
        connectionFactory.setPort(rabbitPort);
        connectionFactory.setUsername(rabbitUsername);
        connectionFactory.setPassword(rabbitPassword);
        return connectionFactory;
    }

    @Bean
    public FanoutExchange exchange() {
        FanoutExchange exchange = (FanoutExchange) ExchangeBuilder.fanoutExchange(exchangeName)
                .durable(exchangeDurable).build();
        return exchange;
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
