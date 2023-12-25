package com.example.week7_springboot.controllers;

import com.example.week7_springboot.models.Product;
import com.example.week7_springboot.serviceImp.OrderServiceImp;
import com.example.week7_springboot.serviceImp.ProductServiceImp;
import com.example.week7_springboot.serviceImp.UsersServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/products")
public class ProductController {
    private static final Logger logger = Logger.getLogger(ProductController.class.getName());

    private ProductServiceImp productService;
    private UsersServiceImp usersService;
    private OrderServiceImp orderService;

    @Autowired
    public ProductController(ProductServiceImp productService, UsersServiceImp usersService, OrderServiceImp orderService) {
        this.productService = productService;
        this.usersService = usersService;
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public ModelAndView findAllProducts(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<Product> productList = productService.findAllProducts.get();
        return new ModelAndView("dashboard")
                .addObject("products", productList)
                .addObject("cartItems", "");
    }

    @GetMapping("/all-cart")
    public ModelAndView viewAllProductsAndCarts(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<Product> productList = productService.findAllProducts.get();
        return new ModelAndView("dashboard")
                .addObject("products", productList)
                .addObject("cartItems", "Cart Items: " + session.getAttribute("cartItems"));
    }

    @GetMapping("/add-cart")
    public String addToCart(@RequestParam(name = "cart") Long id, HttpServletRequest request) {
        productService.addProductToCart(id, request);
        return "redirect:/products/all-cart";
    }


    @GetMapping("/payment")
    public String checkOut(HttpSession session, Model model) {
        if (session.getAttribute("userID") != null) {
            productService.checkOutCart(session, model);
            model.addAttribute("paid", "");
            return "checkout";
        }
        return "redirect:/user/login-payment";
    }

    @GetMapping("/pay")
    public String orderPayment(HttpSession session, Model model) {
        return orderService.makePayment(session, model);
    }




    @GetMapping("/bycategory")
    public ModelAndView getProductsByCategory(@RequestParam String category) {
        try {
            Supplier<List<Product>> categoryList = productService.findProductByCategory(category);
            List<Product> products = categoryList.get();
            return new ModelAndView("dashboard")
                    .addObject("products", products);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getProductByCategory", e);
            return new ModelAndView("error");
        }
    }
}
