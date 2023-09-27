package com.flybuilder.userapi.service;

import com.flybuilder.userapi.model.db.entity.User;
import com.flybuilder.userapi.model.dto.request.UserInfoRequest;
import com.flybuilder.userapi.model.dto.response.CarInfoResponse;
import com.flybuilder.userapi.model.dto.response.UserInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface UserService {

    UserInfoResponse createUser(UserInfoRequest request);

    UserInfoResponse getUserDto(Long id);

    User getUser(Long id);

    UserInfoResponse updateUser(Long id, UserInfoRequest request);

    void deleteUser(Long id);

    List<UserInfoResponse> getAllUsers();

    User updateCarList(User user);

    List<CarInfoResponse> getCarsByUser(Long id);

    Page<UserInfoResponse> getAllUsers(Integer page, Integer perPage, String sort, Sort.Direction order, String filter);

}
