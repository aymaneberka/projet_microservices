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

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // READ - liste
    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    // READ - détail
    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest request) {
        return productService.createProduct(request);
    }

    // UPDATE complet (nom, description, prix, stock)
    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id,
                                         @RequestBody ProductRequest request) {
        return productService.updateProduct(id, request);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    // UPDATE partiel : diminution de stock
    @PutMapping("/{id}/decrease-stock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void decreaseStock(@PathVariable Long id,
                              @RequestParam("quantity") int quantity) {
        productService.decreaseStock(id, quantity);
    }
}
