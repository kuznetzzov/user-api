package com.flybuilder.userapi.service.impl;

import com.flybuilder.userapi.model.dto.request.UserInfoRequest;
import com.flybuilder.userapi.model.dto.response.UserInfoResponse;
import com.flybuilder.userapi.service.CarService;
import com.flybuilder.userapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private List<UserInfoResponse> users = new ArrayList<>();

    @Override
    public UserInfoResponse createUser(UserInfoRequest request) {
        log.warn("User created!");
        UserInfoResponse user = UserInfoResponse.builder()
                .id(100L)
                .age(request.getAge())
                .email(request.getEmail())
                .gender(request.getGender())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .password(request.getPassword())
                .build();
        users.add(user);
        return user;
    }

    @Override
    public UserInfoResponse getUser(Long id) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        for (UserInfoResponse user : users) {
            if (Objects.equals(id, user.getId())){
                userInfoResponse = user;
            }
        }
        return userInfoResponse;
    }

    @Override
    public UserInfoResponse updateUser(Long id, UserInfoRequest request) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {
    }

    @Override
    public List<UserInfoResponse> getAllUsers() {
        return null;
    }
}
