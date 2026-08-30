package com.ecommerce.payment.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.common.entity.PaymentStatus;
import com.ecommerce.payment.dto.CreatePaymentRequest;
import com.ecommerce.payment.dto.PaymentResponse;
import com.ecommerce.payment.mapper.PaymentMapper;
import com.ecommerce.payment.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody CreatePaymentRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PaymentMapper.toResponse(paymentService.createPayment(req)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(PaymentMapper.toResponse(paymentService.getPaymentById(id)));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(PaymentMapper.toResponse(paymentService.getPaymentByOrderId(orderId)));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getByStatus(@RequestParam PaymentStatus status) {
        return ResponseEntity.ok(paymentService.getPaymentsByStatus(status).stream().map(PaymentMapper::toResponse).toList());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentResponse> updateStatus(@PathVariable Long id, @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(PaymentMapper.toResponse(paymentService.updatePaymentStatus(id, status)));
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentResponse> refund(@PathVariable Long id) {
        return ResponseEntity.ok(PaymentMapper.toResponse(paymentService.refundPayment(id)));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestBody String payload) {
        paymentService.handleWebhook(payload);
        return ResponseEntity.ok("Webhook processed");
    }
}
