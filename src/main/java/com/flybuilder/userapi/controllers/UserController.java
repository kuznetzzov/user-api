package com.flybuilder.userapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/hello")
    public String sayHello(){
        return "Hello World!";
    }

    @GetMapping("/someRequest")
    public String something(){
        return "Another request";
    }

    @GetMapping
    public List<String> getUsers(){
        return List.of("ANDREW", "ANNA");
    }
}
