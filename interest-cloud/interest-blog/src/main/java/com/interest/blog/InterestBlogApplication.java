package com.interest.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.interest.blog.commom.feign")
public class InterestBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterestBlogApplication.class, args);
    }

}
