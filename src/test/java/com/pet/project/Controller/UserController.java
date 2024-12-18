package com.pet.project.Controller;

import com.pet.project.Entity.UserT;
import com.pet.project.Interface.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping
    public List<UserT> getUsers() {
        return userRepository.findAll();
    }
}
