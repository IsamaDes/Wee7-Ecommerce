package com.example.week7_springboot.serviceImp;

import com.example.week7_springboot.models.Cart;
import com.example.week7_springboot.models.Order;
import com.example.week7_springboot.models.Product;
import com.example.week7_springboot.repositories.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;


@Service
public class ProductServiceImp {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImp(ProductRepository productRepository ) {
        this.productRepository = productRepository;
    }

    public Supplier<List<Product>> findAllProducts = ()-> productRepository.findAll();



    public Function<Long, Product> findById = (id)->
            productRepository.findById(id)
                    .orElseThrow(()->
                            new NullPointerException("No such product found with ID: "+ id));


    public void addProductToCart(Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Cart cart;
        if (session.getAttribute("cart")!=null){
            cart = (Cart) session.getAttribute("cart");
            cart.setProductIds(cart.getProductIds()+","+ id);
            session.setAttribute("cartItems", cart.getProductIds().split(",").length);
        }
        else {
            cart = Cart.builder().productIds(id.toString())
                    .userId((Long) session.getAttribute("userID")).build();
            session.setAttribute("cart", cart);
            session.setAttribute("cartItems", cart.getProductIds().split(",").length);
        }
    }

    public void checkOutCart(HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");
        List<Product> productList = new ArrayList<>();
        List<String> productIds = Arrays.stream(cart.getProductIds().split(",")).toList();
        productIds.forEach(id->{
            productList.add(productRepository.findById(Long.parseLong(id)).orElseThrow(()->
                    new NullPointerException("No such product found with ID: "+ id)));
        });

        final BigDecimal[] totalPrice = {new BigDecimal(0)};
        productList.forEach(product -> totalPrice[0] = totalPrice[0].add(product.getPrice()));
        model.addAttribute("totalPrice", "Total Price: $"+ totalPrice[0]);
        session.setAttribute("cart", null);
        Order order = Order.builder()
                .productList(productList)
                .userId((Long) session.getAttribute("userID"))
                .totalPrice(totalPrice[0])
                .build();
        session.setAttribute("order", order);
        model.addAttribute("order", order);
    }


    public Supplier<List<Product>> findProductByCategory(String category) {
       return () -> {
             return productRepository.findByCategories(category).get();
       };
    }
}











