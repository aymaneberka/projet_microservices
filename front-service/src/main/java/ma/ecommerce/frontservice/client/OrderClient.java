package ma.ecommerce.frontservice.client;

import ma.ecommerce.frontservice.dto.CreateOrderRequest;
import ma.ecommerce.frontservice.dto.CreateOrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service")
public interface OrderClient {

    @PostMapping("/orders")
    CreateOrderResponse createOrder(@RequestBody CreateOrderRequest request);
}
