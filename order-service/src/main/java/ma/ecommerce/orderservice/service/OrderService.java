package ma.ecommerce.orderservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import ma.ecommerce.orderservice.client.NotificationClient;
import ma.ecommerce.orderservice.client.PaymentClient;
import ma.ecommerce.orderservice.client.ProductClient;
import ma.ecommerce.orderservice.dto.*;
import ma.ecommerce.orderservice.entity.Order;
import ma.ecommerce.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;
    private final NotificationClient notificationClient;

    public OrderService(OrderRepository orderRepository,
                        ProductClient productClient,
                        PaymentClient paymentClient,
                        NotificationClient notificationClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.paymentClient = paymentClient;
        this.notificationClient = notificationClient;
    }

    @Transactional
    @CircuitBreaker(name = "paymentService", fallbackMethod = "createOrderFallback")
    public CreateOrderResponse createOrder(CreateOrderRequest request) {

        log.info("Création commande produit={} qty={} email={}",
                request.getProductId(), request.getQuantity(), request.getCustomerEmail());

        // 1. Produit
        ProductResponse product = productClient.getProductById(request.getProductId());
        double total = product.getPrice() * request.getQuantity();

        // 2. Création commande
        Order order = new Order(
                request.getProductId(),
                request.getQuantity(),
                total,
                "CREATED",
                request.getCustomerEmail()
        );
        order = orderRepository.save(order);

        // 3. Paiement
        PaymentRequest paymentRequest = new PaymentRequest(
                order.getId(),
                total,
                request.getPaymentMethod()
        );
        PaymentResponse paymentResponse = paymentClient.pay(paymentRequest);

        String message;

        // 4. Selon résultat paiement
        if ("SUCCESS".equalsIgnoreCase(paymentResponse.getStatus())) {
            order.setStatus("PAID");
            orderRepository.save(order);

            // 5. Notif email au client
            NotificationRequest notif = new NotificationRequest(
                    order.getId(),
                    "Votre commande a été payée avec succès. Montant : " + order.getTotalAmount(),
                    "EMAIL",
                    order.getCustomerEmail()  // 👈 ici l’email du client
            );
            notificationClient.send(notif);

            message = "Commande payée et notification envoyée";
        } else {
            order.setStatus("FAILED_PAYMENT");
            orderRepository.save(order);
            message = "Échec du paiement";
        }

        return new CreateOrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                paymentResponse.getStatus(),
                message
        );
    }

    @Transactional
    public CreateOrderResponse createOrderFallback(CreateOrderRequest request, Throwable ex) {
        log.error("Fallback paymentService pour produit {} : {}",
                request.getProductId(), ex.getMessage());

        ProductResponse product = productClient.getProductById(request.getProductId());
        double total = product.getPrice() * request.getQuantity();

        Order order = new Order(
                request.getProductId(),
                request.getQuantity(),
                total,
                "PENDING_MANUAL_REVIEW",
                request.getCustomerEmail()
        );
        order = orderRepository.save(order);

        return new CreateOrderResponse(
                order.getId(),
                order.getTotalAmount(),
                "PENDING_MANUAL_REVIEW",
                "PENDING_MANUAL_REVIEW",
                "Service de paiement indisponible, commande en attente de traitement manuel"
        );
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
    }
}
