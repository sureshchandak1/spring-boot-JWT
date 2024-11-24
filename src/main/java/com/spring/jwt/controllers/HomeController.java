package com.spring.jwt.controllers;

import com.spring.jwt.models.User;
import com.spring.jwt.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService mUserService;

    // http://localhost:8081/home/users
    @GetMapping("/users")
    public List<User> getUser() {
        return mUserService.getUsers();
    }

    @GetMapping("/current-user")
    public String getLoggedInUser(Principal principal) {
        return principal.getName();
    }

}
