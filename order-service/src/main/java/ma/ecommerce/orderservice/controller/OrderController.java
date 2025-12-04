package ma.ecommerce.orderservice.controller;

import ma.ecommerce.orderservice.dto.CreateOrderRequest;
import ma.ecommerce.orderservice.dto.CreateOrderResponse;
import ma.ecommerce.orderservice.entity.Order;
import ma.ecommerce.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        CreateOrderResponse response = service.createOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Order order = service.getOrderById(id);
        return ResponseEntity.ok(order);
    }
}
