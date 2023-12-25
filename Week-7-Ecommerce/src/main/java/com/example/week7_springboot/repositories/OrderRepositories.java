package com.example.week7_springboot.repositories;


import com.example.week7_springboot.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepositories extends JpaRepository<Order, Long> {
}