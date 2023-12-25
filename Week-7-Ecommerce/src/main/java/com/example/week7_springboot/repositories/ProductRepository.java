package com.example.week7_springboot.repositories;

import com.example.week7_springboot.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.function.Supplier;


public interface ProductRepository extends JpaRepository<Product, Long>{
    Supplier<List<Product>> findByCategories(String categories);
}





