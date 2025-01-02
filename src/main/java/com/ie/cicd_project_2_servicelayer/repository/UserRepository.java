package com.ie.cicd_project_2_servicelayer.repository;

import com.ie.cicd_project_2_servicelayer.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
