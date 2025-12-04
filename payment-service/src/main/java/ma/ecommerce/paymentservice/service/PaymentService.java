package ma.ecommerce.paymentservice.service;

import ma.ecommerce.paymentservice.dto.PaymentRequest;
import ma.ecommerce.paymentservice.dto.PaymentResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    public PaymentResponse processPayment(PaymentRequest request) {

        boolean success = true;


        String status = success ? "SUCCESS" : "FAILED";
        String transactionId = UUID.randomUUID().toString();

        return new PaymentResponse(
                request.getOrderId(),
                request.getAmount(),
                status,
                transactionId
        );
    }
}
