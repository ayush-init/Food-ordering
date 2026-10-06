package com.foodordering.orderservice;

import com.foodordering.orderservice.client.FoodClient;
import com.foodordering.orderservice.client.PaymentClient;
import com.foodordering.orderservice.dto.*;
import com.foodordering.orderservice.kafka.OrderEventProducer;
import com.foodordering.orderservice.model.Order;
import com.foodordering.orderservice.model.OrderItem;
import com.foodordering.orderservice.repository.OrderItemRepository;
import com.foodordering.orderservice.repository.OrderRepository;
import com.foodordering.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceApplicationTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private FoodClient foodClient;

    @Mock
    private PaymentClient paymentClient;

    @Mock
    private OrderEventProducer orderEventProducer;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testCreateOrderSuccessWorkflow() {
        OrderRequest request = new OrderRequest(1L, 1L, List.of(new OrderItemRequest(10L, 2)));

        FoodResponse food = new FoodResponse(10L, "Burger", 150.0, "Fast Food", true, 1L);
        when(foodClient.getFood(10L)).thenReturn(food);

        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            if (o.getOrderId() == null) o.setOrderId(100L);
            return o;
        });

        PaymentResponse paymentResponse = new PaymentResponse(1L, 100L, 300.0, "SUCCESS", LocalDateTime.now());
        when(paymentClient.processPayment(any(PaymentRequest.class))).thenReturn(paymentResponse);

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals(100L, response.getOrderId());
        assertEquals("PLACED", response.getStatus());
        assertEquals(300.0, response.getTotalAmount());
        assertEquals("SUCCESS", response.getPaymentStatus());

        verify(foodClient, times(1)).getFood(10L);
        verify(paymentClient, times(1)).processPayment(any(PaymentRequest.class));
        verify(orderEventProducer, times(1)).publishOrderCreated(any(OrderCreatedEvent.class));
    }
}
