package canteenManagment.main.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;

import canteenManagment.main.UserEntity.Order;

public class OrderRequest {
    private List<Order> orders;

    @JsonCreator
    public OrderRequest(Object input) {
        if (input instanceof Map) {
            // Single order
            Order singleOrder = new ObjectMapper().convertValue(input, Order.class);
            this.orders = Collections.singletonList(singleOrder);
        } else if (input instanceof List) {
            // Multiple orders
            this.orders = ((List<?>) input).stream()
                .map(item -> new ObjectMapper().convertValue(item, Order.class))
                .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Invalid order input");
        }
    }

    public List<Order> getOrders() {
        return orders;
    }
}
