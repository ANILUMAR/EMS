package canteenManagment.main.userReposerity;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import canteenManagment.main.UserEntity.Payment;
@Repository

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
