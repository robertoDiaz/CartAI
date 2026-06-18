package com.bikemmerce.commerce.infrastructure.out.kafka;

import com.bikemmerce.commerce.domain.model.value.objects.OrderPlacedEvent;
import com.bikemmerce.commerce.domain.ports.events.OrderPlacedEventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderPlacedEventPublisherKafkaAdapter implements OrderPlacedEventPublisherPort {

    private static final String TOPIC = "orders-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(OrderPlacedEvent event) {
        kafkaTemplate.send(TOPIC, event.customerId().value(), event)
                .whenComplete((stringObjectSendResult, throwable) -> {
                    if (throwable != null) {
                        log.error("Error processing order {}", event.orderId().value());
                    }
                });
    }
}
