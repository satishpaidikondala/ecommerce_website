package com.ecommerce.order.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ecommerce.common.event.OrderCreatedEvent;
import com.ecommerce.order.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OutboxPublisher {
    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);
    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public OutboxPublisher(OutboxRepository outboxRepository, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPending() {
        var events = outboxRepository.findByPublishedFalse();
        for (var e : events) {
            try {
                OrderCreatedEvent evt = objectMapper.readValue(e.getPayload(), OrderCreatedEvent.class);
                rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ORDER_CREATED_ROUTING_KEY, evt);
                e.setPublished(true);
                outboxRepository.save(e);
                log.debug("Outbox published event {}", e.getId());
            } catch (Exception ex) {
                log.warn("Outbox publish failed for {}: {}", e.getId(), ex.getMessage());
            }
        }
    }
}
