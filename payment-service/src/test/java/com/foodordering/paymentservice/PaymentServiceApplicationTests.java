package com.foodordering.paymentservice;

import com.foodordering.paymentservice.controller.PaymentController;
import com.foodordering.paymentservice.dto.PaymentRequest;
import com.foodordering.paymentservice.dto.PaymentResponse;
import com.foodordering.paymentservice.model.Payment;
import com.foodordering.paymentservice.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceApplicationTests {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    void testProcessPaymentSuccess() {
        PaymentRequest request = new PaymentRequest(101L, 450.0);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            p.setPaymentId(1L);
            return p;
        });

        ResponseEntity<PaymentResponse> response = paymentController.processPayment(request);

        assertNotNull(response.getBody());
        assertEquals("SUCCESS", response.getBody().getPaymentStatus());
        assertEquals(101L, response.getBody().getOrderId());
        assertEquals(450.0, response.getBody().getAmount());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
}
