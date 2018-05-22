package com.prooptykwebapi.prooptyk.controller;


import com.prooptykwebapi.prooptyk.model.User;
import com.prooptykwebapi.prooptyk.repository.UserRepository;
import com.prooptykwebapi.prooptyk.security.JWTAuthenticationFilter;
import com.prooptykwebapi.prooptyk.service.JWTAuthenticationResponse;
import com.prooptykwebapi.prooptyk.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.ws.Response;
import java.util.Date;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    public UserController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @RequestMapping(value = "/user/registration", method = RequestMethod.GET)
    public ModelAndView showRegistrationForm(WebRequest request, Model model) {
        User user = new User();
        model.addAttribute("user", user);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(path = "/user/registration", method = RequestMethod.POST)
    public String registerUser(@RequestBody User user, HttpServletRequest request) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
       try {
           userService.save(user);
           return "Succesfully saved";
       } catch (Exception e) {
           return "Error";
       }
    }

    @RequestMapping("/users/sign-up")
    public void signUp(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setLastPasswordChange(new Date(System.currentTimeMillis()));
        userService.save(user);
    }

    @RequestMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        jwtAuthenticationFilter.attemptAuthentication(request, response);

    }
}
