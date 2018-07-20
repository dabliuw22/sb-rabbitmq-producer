
package com.leysoft.service.imple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leysoft.model.SimpleMessage;
import com.leysoft.service.inter.SenderService;

@Service
public class SenderServiceImp implements SenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SenderServiceImp.class);

    @Value(
            value = "${rabbitmq.routing-key}")
    private String routingKey;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private Exchange exchange;

    @Override
    public String send(SimpleMessage message) {
        String messageInfo = "Error";
        try {
            messageInfo = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            LOGGER.error("{}", e.getMessage());
        }
        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, message);
        LOGGER.info("send message -> {}", messageInfo);
        return messageInfo;
    }
}
