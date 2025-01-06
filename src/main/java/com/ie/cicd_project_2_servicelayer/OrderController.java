package com.ie.cicd_project_2_servicelayer;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/newOrder")
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
            String createdOrder = orderService.createOrder(order);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<String> getAllOrders() {
        String retrieveAllOrders = orderService.getAllOrders();
        return new ResponseEntity<>(retrieveAllOrders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrderById(@PathVariable Long id) {
        try {
            String retrieveOrderById = orderService.getOrderById(id);
            return new ResponseEntity<>(retrieveOrderById, HttpStatus.OK);
        } catch (FeignException.NotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<String> getOrdersByUserId(@PathVariable Long id) {
        String retrieveOrdersByUserId = orderService.getOrdersByUserId(id);
        return new ResponseEntity<>(retrieveOrdersByUserId, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        try {
            String retrieveUpdatedOrder = orderService.updateOrder(id, orderDetails);
            return new ResponseEntity<>(retrieveUpdatedOrder, HttpStatus.OK);
        } catch (FeignException.NotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        try {
            String deletedOrder = orderService.deleteOrder(id);
            return new ResponseEntity<>(deletedOrder, HttpStatus.NO_CONTENT);
        } catch (FeignException.NotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
