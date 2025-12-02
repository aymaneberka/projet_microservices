package ma.ecommerce.productservice.service;

import ma.ecommerce.productservice.dto.ProductRequest;
import ma.ecommerce.productservice.dto.ProductResponse;
import ma.ecommerce.productservice.entity.Product;
import ma.ecommerce.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<ProductResponse> getAll() {
        return repo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductResponse getById(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found " + id));
        return mapToResponse(p);
    }

    public ProductResponse create(ProductRequest request) {
        Product p = new Product(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock()
        );
        p = repo.save(p);
        return mapToResponse(p);
    }

    public void decreaseStock(Long id, int quantity) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found " + id));

        if (p.getStock() < quantity) {
            throw new RuntimeException("Stock insuffisant");
        }
        p.setStock(p.getStock() - quantity);
        repo.save(p);
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock()
        );
    }
}
