package com.ie.cicd_project_2_servicelayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CicdProject2ServiceLayerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CicdProject2ServiceLayerApplication.class, args);
    }

}
