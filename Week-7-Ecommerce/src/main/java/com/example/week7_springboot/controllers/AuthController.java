package com.example.week7_springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/////in this application, you will have to order for products in your cart before you can login

    @Controller
    public class AuthController {

        @GetMapping
        public String index(){
            return "index";
        }
    }


