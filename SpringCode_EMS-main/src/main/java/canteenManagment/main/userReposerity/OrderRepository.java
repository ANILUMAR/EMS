package canteenManagment.main.userReposerity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import canteenManagment.main.UserEntity.Order;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
   List<Order> findByStatus(String status);
}