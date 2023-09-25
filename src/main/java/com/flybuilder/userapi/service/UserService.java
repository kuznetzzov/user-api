package com.flybuilder.userapi.service;

import com.flybuilder.userapi.model.dto.request.UserInfoRequest;
import com.flybuilder.userapi.model.dto.response.UserInfoResponse;

import java.util.List;

public interface UserService {

    UserInfoResponse createUser(UserInfoRequest request);

    UserInfoResponse getUser(Long id);

    UserInfoResponse updateUser(Long id, UserInfoRequest request);

    void deleteUser(Long id);

    List<UserInfoResponse> getAllUsers();

}
