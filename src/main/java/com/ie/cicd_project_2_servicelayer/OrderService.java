package com.ie.cicd_project_2_servicelayer;

import com.ie.cicd_project_2_servicelayer.repository.NotificationLayerClient;
import com.ie.cicd_project_2_servicelayer.repository.OrderRepository;
import com.ie.cicd_project_2_servicelayer.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final NotificationLayerClient notificationLayerClient;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        NotificationLayerClient notificationLayerClient) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.notificationLayerClient = notificationLayerClient;
    }

    @Transactional
    public String createOrder(Order newOrder) {
        newOrder.setOrderDate(LocalDateTime.now());
        double totalPrice = 0;
        for(OrderItem item : newOrder.getOrderItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("ERROR: Product not found"));
            if(product.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("ERROR: Insufficient stock for product: "
                        + product.getName());
            }
            item.setProduct(product);
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
            totalPrice += product.getPrice() * item.getQuantity();
        }
        newOrder.setTotalPrice(totalPrice);
        newOrder.setOrderDate(LocalDateTime.now());
        Order savedOrder = orderRepository.save(newOrder);
        return notificationLayerClient.notifyOrderCreated(savedOrder);
    }

    public String getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return notificationLayerClient.notifyAllOrders(orders);
    }

    public String getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ERROR: Order not found"));
        return notificationLayerClient.notifyOrderById(order);
    }

    public String getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return notificationLayerClient.notifyOrdersByUserId(userId, orders);
    }

    @Transactional
    public String updateOrder(Long id, Order orderDetails) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ERROR: Order not found"));
        order.setUserId(orderDetails.getUserId());
        order.setOrderItems(orderDetails.getOrderItems());
        order.setTotalPrice(orderDetails.getTotalPrice());

        Order updatedOrder = orderRepository.save(order);
        return notificationLayerClient.notifyOrderUpdated(updatedOrder);
    }

    @Transactional
    public String deleteOrder(Long id) {
        orderRepository.deleteById(id);
        return notificationLayerClient.notifyOrderDeleted(id);
    }
}
