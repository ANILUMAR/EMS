package canteenManagment.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import canteenManagment.main.UserEntity.Order;
import canteenManagment.main.userReposerity.OrderRepository;

@Service
public class OrderServiceImpl implements orderServiceInterface {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> getConfirmedOrders() {
        return orderRepository.findByStatus("PENDING");
    }
}
