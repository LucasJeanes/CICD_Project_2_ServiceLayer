package com.ie.cicd_project_2_servicelayer.repository;

import com.ie.cicd_project_2_servicelayer.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
