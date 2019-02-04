
package com.leysoft.service.imple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leysoft.model.SimpleMessage;
import com.leysoft.service.inter.SenderService;

@Service
public class SenderServiceImp implements SenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SenderServiceImp.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private Exchange exchange;

    @Override
    @Transactional
    public String send(SimpleMessage message) {
        String messageInfo;
        try {
            messageInfo = objectMapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend(exchange.getName(), "", message);
        } catch (JsonProcessingException | AmqpException e) {
            LOGGER.error("{}", e.getMessage());
            messageInfo = "Error";
        }
        LOGGER.info("send message -> {}", messageInfo);
        return messageInfo;
    }
}
