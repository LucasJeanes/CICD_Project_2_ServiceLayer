package com.ie.cicd_project_2_servicelayer;

import com.ie.cicd_project_2_servicelayer.repository.NotificationLayerClient;
import com.ie.cicd_project_2_servicelayer.repository.OrderRepository;
import com.ie.cicd_project_2_servicelayer.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private NotificationLayerClient notificationLayerClient;
    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void openMocks() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateOrder() {
        Product product = new Product(1L,
                "Test Product",
                100.0,
                "Description",
                10);
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        Order newOrder = new Order();
        newOrder.setUserId(1L);
        newOrder.setOrderItems(Collections.singletonList(orderItem));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setId(1L);
            savedOrder.setTotalPrice(200.0);
            savedOrder.setOrderDate(LocalDateTime.now());
            product.setQuantity(8);
            return savedOrder;
        });

        when(notificationLayerClient.notifyOrderCreated(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order createdOrder = invocation.getArgument(0);
                    return "Order created with ID: " + createdOrder.getId() + createdOrder;
                });

        String result = orderService.createOrder(newOrder);

        assertTrue(result.startsWith("Order created with ID: 1"));
        assertTrue(result.contains("totalPrice=200.0"));
        assertTrue(result.contains("quantity=8"));

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(product);
        verify(orderRepository, times(1)).save(newOrder);
        verify(notificationLayerClient, times(1)).notifyOrderCreated(newOrder);
    }

    @Test
    void testGetAllOrders() {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(1L);

        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));
        when(notificationLayerClient.notifyAllOrders(any()))
                .thenReturn("Retrieved all 1 orders. \n" + Collections.singletonList(order));

        String result = orderService.getAllOrders();

        assertEquals("Retrieved all 1 orders. \n" + Collections.singletonList(order), result);
        verify(orderRepository, times(1)).findAll();
        verify(notificationLayerClient, times(1)).notifyAllOrders(Collections.singletonList(order));
    }

    @Test
    void testGetOrderById() {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(notificationLayerClient.notifyOrderById(any(Order.class)))
                .thenReturn("Retrieved order with ID: " + order.getId() + order);

        String result = orderService.getOrderById(1L);

        assertEquals("Retrieved order with ID: " + order.getId() + order, result);
        verify(orderRepository, times(1)).findById(1L);
        verify(notificationLayerClient, times(1)).notifyOrderById(order);
    }

    @Test
    void testGetOrdersByUserId() {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(1L);

        when(orderRepository.findByUserId(1L)).thenReturn(Collections.singletonList(order));
        when(notificationLayerClient.notifyOrdersByUserId(eq(1L), any()))
                .thenReturn("Retrieved 1 order(s) for user ID: 1" + Collections.singletonList(order));

        String result = orderService.getOrdersByUserId(1L);

        assertEquals("Retrieved 1 order(s) for user ID: 1" + Collections.singletonList(order), result);
        verify(orderRepository, times(1)).findByUserId(1L);
        verify(notificationLayerClient, times(1)).notifyOrdersByUserId(eq(1L), any());
    }

    @Test
    void testUpdateOrder() {
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setUserId(1L);
        existingOrder.setTotalPrice(100.0);
        existingOrder.setOrderDate(LocalDateTime.now());

        Order updatedOrder = new Order();
        updatedOrder.setUserId(2L);
        updatedOrder.setTotalPrice(200.0);
        updatedOrder.setOrderItems(Collections.emptyList()); // or set some order items if needed

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setUserId(updatedOrder.getUserId());
            savedOrder.setOrderItems(updatedOrder.getOrderItems());
            savedOrder.setTotalPrice(updatedOrder.getTotalPrice());
            return savedOrder;
        });

        when(notificationLayerClient.notifyOrderUpdated(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order notifiedOrder = invocation.getArgument(0);
                    return "Updated order with ID: " + notifiedOrder.getId() + notifiedOrder;
                });

        String result = orderService.updateOrder(1L, updatedOrder);

        assertTrue(result.startsWith("Updated order with ID: 1"));
        assertTrue(result.contains("userId=2"));
        assertTrue(result.contains("totalPrice=200.0"));

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(notificationLayerClient, times(1)).notifyOrderUpdated(any(Order.class));
    }

    @Test
    void testDeleteOrder() {
        Long orderId = 1L;

        when(notificationLayerClient.notifyOrderDeleted(orderId))
                .thenReturn("Deleted order with ID: " + orderId);

        String result = orderService.deleteOrder(orderId);

        assertEquals("Deleted order with ID: 1", result);
        verify(orderRepository, times(1)).deleteById(orderId);
        verify(notificationLayerClient, times(1)).notifyOrderDeleted(orderId);
    }
}
