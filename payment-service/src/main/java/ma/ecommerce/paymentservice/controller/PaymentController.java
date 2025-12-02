package ma.ecommerce.paymentservice.controller;

import ma.ecommerce.paymentservice.dto.PaymentRequest;
import ma.ecommerce.paymentservice.dto.PaymentResponse;
import ma.ecommerce.paymentservice.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse pay(@RequestBody PaymentRequest request) {
        return paymentService.processPayment(request);
    }
}
