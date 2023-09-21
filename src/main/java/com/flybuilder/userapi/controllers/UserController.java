package com.flybuilder.userapi.controllers;

import com.flybuilder.userapi.model.dto.request.UserInfoRequest;
import com.flybuilder.userapi.model.dto.response.UserInfoResponse;
import com.flybuilder.userapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping
    public UserInfoResponse createUser(@RequestBody UserInfoRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public UserInfoResponse getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    public UserInfoResponse updateUser(@PathVariable Long id, @RequestBody UserInfoRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }


    @GetMapping("/all")
    public List<UserInfoResponse> getAllUsers () {
        return userService.getAllUsers();
    }

}
