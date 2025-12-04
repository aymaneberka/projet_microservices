package ma.ecommerce.frontservice.service;

import feign.FeignException;
import ma.ecommerce.frontservice.client.OrderClient;
import ma.ecommerce.frontservice.client.ProductClient;
import ma.ecommerce.frontservice.dto.ClientDto;
import ma.ecommerce.frontservice.dto.CreateOrderRequest;
import ma.ecommerce.frontservice.dto.CreateOrderResponse;
import ma.ecommerce.frontservice.dto.ProductDto;
import ma.ecommerce.frontservice.exception.FrontendException;
import ma.ecommerce.frontservice.web.OrderForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    private final ProductClient productClient;
    private final OrderClient orderClient;

    public ShopService(ProductClient productClient, OrderClient orderClient) {
        this.productClient = productClient;
        this.orderClient = orderClient;
    }

    public List<ProductDto> getProducts() {
        try {
            return productClient.getProducts();
        } catch (FeignException ex) {
            throw new FrontendException("Produits indisponibles pour le moment", ex);
        }
    }

    public ProductDto getProduct(Long id) {
        try {
            return productClient.getProductById(id);
        } catch (FeignException ex) {
            throw new FrontendException("Produit introuvable ou service indisponible", ex);
        }
    }

    public CreateOrderResponse placeOrder(OrderForm form) {
        ClientDto client = new ClientDto();
        client.setFirstName(form.getFirstName());
        client.setLastName(form.getLastName());
        client.setEmail(form.getEmail());

        CreateOrderRequest request = new CreateOrderRequest();
        request.setProductId(form.getProductId());
        request.setQuantity(form.getQuantity() == null ? 1 : form.getQuantity());
        request.setPaymentMethod(form.getPaymentMethod());
        request.setClient(client);

        try {
            return orderClient.createOrder(request);
        } catch (FeignException ex) {
            String message = ex.status() >= 500
                    ? "Commande impossible pour le moment (service)."
                    : "Commande rejetee, verifiez les informations saisies.";
            throw new FrontendException(message, ex);
        } catch (Exception ex) {
            throw new FrontendException("Erreur interne lors de la commande", ex);
        }
    }
}
