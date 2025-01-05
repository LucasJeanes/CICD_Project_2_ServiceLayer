package com.ie.cicd_project_2_servicelayer;

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

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrder(Order newOrder) {
        newOrder.setOrderDate(LocalDateTime.now());
        double totalPrice = 0;
        for(OrderItem item : newOrder.getOrderItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("ERROR: Product not found"));
            if(product.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("ERROR: Insufficient stock for product: " + product.getName());
            }
            item.setProduct(product);
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
            totalPrice += product.getPrice() * item.getQuantity();
        }
        newOrder.setTotalPrice(totalPrice);
        newOrder.setOrderDate(LocalDateTime.now());
        return orderRepository.save(newOrder);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ERROR: Order not found"));
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public Order updateOrder(Long id, Order orderDetails) {
        Order order = getOrderById(id);
        order.setUserId(orderDetails.getUserId());
        order.setOrderItems(orderDetails.getOrderItems());
        order.setTotalPrice(orderDetails.getTotalPrice());
        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
