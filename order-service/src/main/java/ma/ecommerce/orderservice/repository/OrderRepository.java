package ma.ecommerce.orderservice.repository;

import ma.ecommerce.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
