package com.spring.jwt.services;

import com.spring.jwt.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();

    public UserService() {
        users.add(new User(UUID.randomUUID().toString(), "Rakesh", "rakesh@gmail.com"));
        users.add(new User(UUID.randomUUID().toString(), "Mohan", "mohan@gmail.com"));
        users.add(new User(UUID.randomUUID().toString(), "Sohan", "sohan@gmail.com"));
        users.add(new User(UUID.randomUUID().toString(), "Sachin", "sachin@gmail.com"));
    }

    public List<User> getUsers() {
        return users;
    }
}
