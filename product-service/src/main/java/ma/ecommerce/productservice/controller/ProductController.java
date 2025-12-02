package ma.ecommerce.productservice.controller;

import ma.ecommerce.productservice.dto.ProductRequest;
import ma.ecommerce.productservice.dto.ProductResponse;
import ma.ecommerce.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@RequestBody ProductRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}/decrease-stock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void decreaseStock(@PathVariable Long id,
                              @RequestParam int quantity) {
        service.decreaseStock(id, quantity);
    }
}
