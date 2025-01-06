package com.ie.cicd_project_2_servicelayer.repository;

import com.ie.cicd_project_2_servicelayer.Order;
import com.ie.cicd_project_2_servicelayer.Product;
import com.ie.cicd_project_2_servicelayer.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "notification-layer", url = "http://localhost:8083")
public interface NotificationLayerClient {
    //USER API CONNECTIONS-----
    @PostMapping("/notify/user/created")
    String notifyUserCreated(@RequestBody User userDetails);

    @PostMapping("/notify/user/all")
    String notifyAllUsers(@RequestBody List<User> users);

    @PostMapping("/notify/user/byId")
    String notifyUserById(@RequestBody User user);

    @PostMapping("/notify/user/updated")
    String notifyUserUpdated(@RequestBody User user);

    @PostMapping("/notify/user/deleted")
    String notifyUserDeleted(@RequestParam Long id);
    //-----USER API METHODS

    //PRODUCT API METHODS-----
    @PostMapping("/notify/product/created")
    String notifyProductCreated(@RequestBody Product productDetails);

    @PostMapping("/notify/product/all")
    String notifyAllProducts(@RequestBody List<Product> products);

    @PostMapping("/notify/product/byId")
    String notifyProductById(@RequestBody Product product);

    @PostMapping("/notify/product/updated")
    String notifyProductUpdated(@RequestBody Product product);

    @PostMapping("/notify/product/deleted")
    String notifyProductDeleted(@RequestParam Long id);
    //-----PRODUCT API METHODS

    //ORDER API METHODS-----
    @PostMapping("/notify/order/created")
    String notifyOrderCreated(@RequestBody Order orderDetails);

    @PostMapping("/notify/order/all")
    String notifyAllOrders(@RequestBody List<Order> orders);

    @PostMapping("/notify/order/byId")
    String notifyOrderById(@RequestBody Order order);

    @PostMapping("/notify/order/byUserId")
    String notifyOrdersByUserId(@RequestParam Long userId, @RequestBody List<Order> orders);

    @PostMapping("/notify/order/updated")
    String notifyOrderUpdated(@RequestBody Order order);

    @PostMapping("/notify/order/deleted")
    String notifyOrderDeleted(@RequestParam Long id);
    //-----ORDER API METHODS
}
