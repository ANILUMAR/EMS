package canteenManagment.main.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import canteenManagment.main.UserEntity.*;
import canteenManagment.main.userReposerity.OrderRepository;
import canteenManagment.main.userReposerity.PaymentRepository;
import canteenManagment.main.userReposerity.UserRepository;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OrderRepository orderRepo;

    @PostMapping("/make")
    public Payment makePayment(@RequestBody Payment payment) {
        // Auto-set date
        payment.setPaymentDate(LocalDateTime.now());
        return paymentRepo.save(payment);
    }
    
    @GetMapping("/all")
    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }
    
    // Get payment by ID

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentRepo.findById(id)
                .orElseThrow();
        return ResponseEntity.ok(payment);
    }
	/*
	 * @PostMapping("/makep") public ResponseEntity<?> makePayment1(@RequestBody
	 * Payment payment) { Optional<UserEntity> userOpt =
	 * userRepo.findById(payment.getUser().getId()); Optional<Order> orderOpt =
	 * orderRepo.findById(payment.getOrder().getOrderId());
	 * 
	 * if (!userOpt.isPresent() || !orderOpt.isPresent()) { return
	 * ResponseEntity.badRequest().body("User or Order not found."); }
	 * 
	 * payment.setUser(userOpt.get()); payment.setOrder(orderOpt.get());
	 * payment.setPaymentDate(LocalDateTime.now());
	 * 
	 * return ResponseEntity.ok(paymentRepo.save(payment)); }
	 */
}
