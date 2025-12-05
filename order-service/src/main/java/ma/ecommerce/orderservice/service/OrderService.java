package ma.ecommerce.orderservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import ma.ecommerce.orderservice.client.NotificationClient;
import ma.ecommerce.orderservice.client.PaymentClient;
import ma.ecommerce.orderservice.client.ProductClient;
import ma.ecommerce.orderservice.dto.*;
import ma.ecommerce.orderservice.entity.Client;
import ma.ecommerce.orderservice.entity.Order;
import ma.ecommerce.orderservice.repository.ClientRepository;
import ma.ecommerce.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;
    private final NotificationClient notificationClient;

    public OrderService(OrderRepository orderRepository,
                        ClientRepository clientRepository,
                        ProductClient productClient,
                        PaymentClient paymentClient,
                        NotificationClient notificationClient) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.productClient = productClient;
        this.paymentClient = paymentClient;
        this.notificationClient = notificationClient;
    }

    @Transactional
    @CircuitBreaker(name = "paymentService", fallbackMethod = "createOrderFallback")
    public CreateOrderResponse createOrder(CreateOrderRequest request) {

        // 1) Récupérer / créer le client à partir du formulaire
        ClientDto dto = request.getClient();
        if (dto == null || dto.getEmail() == null) {
            throw new RuntimeException("Les informations client (nom, prénom, email) sont obligatoires");
        }

        Client client = clientRepository.findByEmail(dto.getEmail())
                .orElseGet(() -> clientRepository.save(
                        new Client(dto.getFirstName(), dto.getLastName(), dto.getEmail())
                ));

        // 2) Récupérer le produit et calculer le total
        ProductResponse product = productClient.getProductById(request.getProductId());
        double total = product.getPrice() * request.getQuantity();

        // 3) Créer la commande
        Order order = new Order(
                request.getProductId(),
                request.getQuantity(),
                total,
                "CREATED",
                client
        );
        order = orderRepository.save(order);

        // 4) Appeler le service de paiement
        PaymentRequest paymentRequest = new PaymentRequest(
                order.getId(),
                total,
                request.getPaymentMethod()
        );
        PaymentResponse paymentResponse = paymentClient.pay(paymentRequest);

        String message;

        if ("SUCCESS".equalsIgnoreCase(paymentResponse.getStatus())) {

            // On retire le stock seulement si le paiement est OK
            productClient.decreaseStock(request.getProductId(), request.getQuantity());

            order.setStatus("PAID");
            orderRepository.save(order);

            // 5) Construire le message personnalisé
            String fullName = client.getFirstName() + " " + client.getLastName();
            String mailMessage =
                    "Bonjour " + fullName + ",\n\n" +
                            "Votre commande #" + order.getId() + " a été payée avec succès.\n" +
                            "Montant : " + order.getTotalAmount() + " MAD.\n\n" +
                            "Merci pour votre confiance.";

            // 6) Envoyer la notification email
            NotificationRequest notif = new NotificationRequest(
                    order.getId(),
                    mailMessage,
                    "EMAIL",
                    client.getEmail()
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

    // Fallback Resilience4J en cas de panne du Payment Service
    public CreateOrderResponse createOrderFallback(CreateOrderRequest request, Throwable throwable) {

        // On crée une commande en "PENDING_MANUAL_REVIEW"
        ClientDto dto = request.getClient();
        Client client = new Client(dto.getFirstName(), dto.getLastName(), dto.getEmail());
        client = clientRepository.save(client);

        ProductResponse product = productClient.getProductById(request.getProductId());
        double total = product.getPrice() * request.getQuantity();

        Order order = new Order(
                request.getProductId(),
                request.getQuantity(),
                total,
                "PENDING_MANUAL_REVIEW",
                client
        );
        order = orderRepository.save(order);

        String fullName = client.getFirstName() + " " + client.getLastName();

        return new CreateOrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                "PENDING_MANUAL_REVIEW",
                "Le service de paiement est indisponible (non demarre). La commande #" + order.getId()
                        + " pour " + fullName + " est enregistree et reste en attente de validation manuelle. Aucun paiement n'a ete effectue."
        );
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
    }

}
