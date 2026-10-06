package com.foodordering.notificationservice;

import com.foodordering.notificationservice.dto.OrderCreatedEvent;
import com.foodordering.notificationservice.kafka.OrderNotificationConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class NotificationServiceApplicationTests {

    @InjectMocks
    private OrderNotificationConsumer consumer;

    @Test
    void testHandleOrderCreatedEvent() {
        OrderCreatedEvent event = new OrderCreatedEvent(101L, 1L, 500.0, "PLACED");
        assertDoesNotThrow(() -> consumer.handleOrderCreated(event));
    }
}
