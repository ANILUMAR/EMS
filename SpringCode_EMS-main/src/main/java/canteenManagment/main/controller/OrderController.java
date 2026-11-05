package canteenManagment.main.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import canteenManagment.main.UserEntity.MenuItem;
import canteenManagment.main.UserEntity.Order;
import canteenManagment.main.UserEntity.OrderItem;
import canteenManagment.main.service.OrderService;
import canteenManagment.main.userReposerity.OrderRepository;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	@Autowired
	OrderRepository orderRepository ;
	@Autowired
	OrderService orderService;

 
	@PostMapping("/orders")
	public ResponseEntity<?> createOrder(@RequestBody Object requestBody) {
	    try {
	        if (requestBody instanceof Map) {
	            // Single Order case
	            Order order = orderService.convertToOrder((Map<String, Object>) requestBody); // ✅ Not OrderItem
	            orderRepository.save(order);
	            return ResponseEntity.ok(new ApiResponse("Single order placed successfully", true));

	        } else if (requestBody instanceof List) {
	            // Multiple Orders case
	            List<Order> orders = orderService.convertToOrderList((List<Object>) requestBody);
	            orderRepository.saveAll(orders);
	            return ResponseEntity.ok(new ApiResponse("Multiple orders placed successfully", true));

	        } else {
	            // Invalid request format
	            return ResponseEntity.badRequest().body("Invalid request format");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("Error processing the request: " + e.getMessage());
	    }
	}

	@GetMapping
    public List<Order> getOrder() {
        return orderRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody  Order order) {
    	  Optional<Order> order1 = orderRepository.findById(id);
    	  if(order1.isPresent()) {
    		  Order orderobject =  order1.get();
    		  orderobject.setStatus(order.getStatus());
    		  orderobject.setTotalPrice(order.getTotalPrice());
    		  orderobject.setOrderDate(order.getOrderDate());
    		  orderRepository.save(orderobject);
    	  }
        return ResponseEntity.ok("Order status updated");
     }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        if (orderRepository.existsById(id)) {
        	orderRepository.deleteById(id);
            return ResponseEntity.ok("Order deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Autowired
    private canteenManagment.main.service.orderServiceInterface orderServiceInterface;

    @GetMapping("/orders/confirmed")
    public ResponseEntity<List<Order>> getConfirmedOrders() {
        List<Order> confirmedOrders = orderServiceInterface.getConfirmedOrders();
        return ResponseEntity.ok(confirmedOrders);
    }
   
}
