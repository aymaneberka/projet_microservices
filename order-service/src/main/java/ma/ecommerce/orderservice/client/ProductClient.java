package ma.ecommerce.orderservice.client;

import ma.ecommerce.orderservice.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/products/{id}")
    ProductResponse getProductById(@PathVariable("id") Long id);

    @PutMapping("/products/{id}/decrease-stock")
    void decreaseStock(@PathVariable("id") Long id,
                       @RequestParam("quantity") int quantity);
}
