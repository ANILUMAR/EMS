package canteenManagment.main.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import canteenManagment.main.UserEntity.Order;
import canteenManagment.main.UserEntity.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
@Service
public class OrderService {

    private final ObjectMapper objectMapper = new ObjectMapper();

	/*
	 * public List<Order> convertToOrderList(List<Object> requestBody) { return
	 * ((List<Object>) requestBody).stream() // Explicit casting .map(item ->
	 * objectMapper.convertValue(item, Order.class)) // Converting each item to
	 * Order .collect(Collectors.toList()); // Collecting into a List<Order> }
	 * 
	 * public Order convertToOrder(Map<String, Object> requestBody) { return
	 * objectMapper.convertValue(requestBody, Order.class); }
	 */
    
    public Order convertToOrder(Map<String, Object> orderData) {
        Order order = new Order();

        // 🧾 Parse totalPrice
        Object totalPriceObj = orderData.get("totalPrice");
        BigDecimal totalPrice = new BigDecimal(totalPriceObj.toString());
        order.setTotalPrice(totalPrice);

        // 🕒 Parse orderDate
        String orderDateStr = orderData.get("orderDate").toString();
        LocalDateTime orderDate = LocalDateTime.parse(orderDateStr, DateTimeFormatter.ISO_DATE_TIME);
        order.setOrderDate(orderDate);

        // 📦 Parse items
        List<Map<String, Object>> itemList = (List<Map<String, Object>>) orderData.get("items");
        List<OrderItem> items = new ArrayList<>();

        for (Map<String, Object> itemMap : itemList) {
            OrderItem item = new OrderItem();
            //item.setId(Long.valueOf(itemMap.get("id").toString()));
            item.setName(itemMap.get("name").toString());
            item.setCategory(itemMap.get("category").toString());
            item.setQuantity(Integer.parseInt(itemMap.get("quantity").toString()));
            item.setPrice(new BigDecimal(itemMap.get("price").toString()));
            
            // 🔄 Set bi-directional link if needed
            item.setOrder(order);
            items.add(item);
        }

        order.setItems(items);
        order.setStatus("PENDING"); // or whatever default you want

        return order;
    }

    public List<Order> convertToOrderList(List<Object> orderListData) {
        List<Order> orders = new ArrayList<>();

        for (Object orderData : orderListData) {
            if (orderData instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> orderMap = (Map<String, Object>) orderData;
                Order order = convertToOrder(orderMap); // reuse existing method
                orders.add(order);
            }
        }

        return orders;
    }
  

}
