package ma.ecommerce.productservice.config;

import ma.ecommerce.productservice.entity.Product;
import ma.ecommerce.productservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initProducts(ProductRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Product("PC Portable", "Laptop 15 pouces", 12000.0, 10));
                repo.save(new Product("Smartphone", "Téléphone Android", 5000.0, 25));
                repo.save(new Product("Casque Audio", "Casque Bluetooth", 800.0, 50));
            }
        };
    }
}
