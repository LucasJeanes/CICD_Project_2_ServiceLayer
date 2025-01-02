package com.ie.cicd_project_2_servicelayer.repository;

import com.ie.cicd_project_2_servicelayer.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
