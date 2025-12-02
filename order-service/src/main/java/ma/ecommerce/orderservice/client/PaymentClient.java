package ma.ecommerce.orderservice.client;

import ma.ecommerce.orderservice.dto.PaymentRequest;
import ma.ecommerce.orderservice.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/payments")
    PaymentResponse pay(@RequestBody PaymentRequest request);
}
