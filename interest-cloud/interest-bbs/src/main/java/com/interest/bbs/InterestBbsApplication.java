package com.interest.bbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.interest.bbs.commom.feign")
public class InterestBbsApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterestBbsApplication.class, args);
    }

}
