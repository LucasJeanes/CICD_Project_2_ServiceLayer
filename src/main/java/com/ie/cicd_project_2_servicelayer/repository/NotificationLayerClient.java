package com.ie.cicd_project_2_servicelayer.repository;

import com.ie.cicd_project_2_servicelayer.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "notification-layer", url = "http://localhost:8083")
public interface NotificationLayerClient {

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
}
