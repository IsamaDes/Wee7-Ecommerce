package com.example.week7_springboot.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.week7_springboot.models.Product;
import com.example.week7_springboot.models.Users;
import com.example.week7_springboot.repositories.ProductRepository;
import com.example.week7_springboot.repositories.UsersRepositories;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

@Component
public class CSVUtils {

        private UsersRepositories usersRepositories;
        private ProductRepository productRepository;

        @Autowired
        public CSVUtils(UsersRepositories usersRepositories, ProductRepository productRepository) {
            this.usersRepositories = usersRepositories;
            this.productRepository = productRepository;
        }

        @PostConstruct
        public void readUserCSV(){

            //user database seeding
            try(BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/java/com/example/week7_springboot/users.csv"))) {
                String line;
                boolean lineOne = false;
                while ((line=bufferedReader.readLine())!=null){
                    String[]user = line.split(",");
                    if (lineOne) {
                        Users user1 = Users.builder().fullName(user[1])
                                .imageUrl(user[3])
                                .password(BCrypt.withDefaults()
                                        .hashToString(12, user[2].trim().toCharArray()))
                                .username(user[0])
                                .balance(new BigDecimal(2500000))
                                .build();
                        usersRepositories.save(user1);
                    }
                    lineOne = true;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //product database seeding
            try(BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/java/com/example/week7_springboot/product.csv"))) {
                String line;
                boolean lineOne = false;
                while ((line=bufferedReader.readLine())!=null){
                    String[]product = line.split(",");
                    if (lineOne) {
                        Product product1 = Product.builder()
                                .categories(product[0])
                                .price(new BigDecimal(product[1]))
                                .productName(product[2])
                                .quantity(Long.parseLong(product[3]))
                                .build();
                        productRepository.save(product1);
                    }
                    lineOne = true;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

