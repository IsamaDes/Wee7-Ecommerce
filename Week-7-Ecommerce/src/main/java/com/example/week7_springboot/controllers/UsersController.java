package com.example.week7_springboot.controllers;

import com.example.week7_springboot.dtos.PasswordDto;
import com.example.week7_springboot.dtos.UsersDto;
import com.example.week7_springboot.models.Users;
import com.example.week7_springboot.serviceImp.UsersServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
@Slf4j
public class UsersController {
    private UsersServiceImp usersService;

    @Autowired
    public UsersController(UsersServiceImp usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String indexPage(Model model){
        model.addAttribute("user", new UsersDto());
        return "sign-up";
    }

    @GetMapping("/login")
    public ModelAndView loginPage(){

        return  new ModelAndView("login")
                .addObject("user", new UsersDto());
    }
    @GetMapping("/login-payment")
    public ModelAndView loginPaymentPage(){

        return  new ModelAndView("login1")
                .addObject("user", new UsersDto());
    }


    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute UsersDto usersDto){
        Users user = usersService.saveUser.apply(new Users(usersDto));
        log.info("User details ---> {}", user);
        return "successful-register";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute UsersDto usersDto, @RequestParam(name = "cart") String something, HttpServletRequest request){
        System.out.println(usersDto);
        Users user = usersService.findUsersByUsername.apply(usersDto.getUsername());
        log.info("User details ---> {}", user);
        if (usersService.verifyUserPassword
                .apply(PasswordDto.builder()
                        .password(usersDto.getPassword())
                        .hashPassword(user.getPassword())
                        .build())){
            HttpSession session = request.getSession();
            session.setAttribute("userID", user.getId());
            if (something!=null){
                return "redirect:/products/all-cart";
            }
            return "redirect:/products/all";
        }
        return "redirect:/user/login";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }

}
