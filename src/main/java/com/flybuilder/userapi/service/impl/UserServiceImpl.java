package com.flybuilder.userapi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flybuilder.userapi.exceptions.CustomException;
import com.flybuilder.userapi.model.db.entity.User;
import com.flybuilder.userapi.model.db.repository.UserRepo;
import com.flybuilder.userapi.model.dto.request.UserInfoRequest;
import com.flybuilder.userapi.model.dto.response.CarInfoResponse;
import com.flybuilder.userapi.model.dto.response.UserInfoResponse;
import com.flybuilder.userapi.model.enums.UserStatus;
import com.flybuilder.userapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.flybuilder.userapi.utils.PaginationUtil.getPageRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final ObjectMapper mapper;
    private final static String errorStr = "User with id %d not found";
    private final static String errorEmail = "Invalid email";

    @Override
    public UserInfoResponse createUser(UserInfoRequest request) {

        String email = request.getEmail();
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new CustomException(errorEmail, HttpStatus.BAD_REQUEST);
        }

        userRepo.findByEmail(email).ifPresent(u -> {
            throw new CustomException("User already exists", HttpStatus.BAD_REQUEST);
        });

        User user = mapper.convertValue(request, User.class);
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(UserStatus.CREATED);

        User save = userRepo.save(user);

        return mapper.convertValue(save, UserInfoResponse.class);
    }

    @Override
    public UserInfoResponse getUserDto(Long id) {
        UserInfoResponse userInfoResponse;
        if (id != 0) {
            User user = getUser(id);
            userInfoResponse = mapper.convertValue(user, UserInfoResponse.class);
        } else {
            throw new CustomException(errorStr, HttpStatus.NOT_FOUND);
        }
        return userInfoResponse;
    }

    @Override
    public User getUser(Long id) {
        final String errMsg = String.format(errorStr, id);
        return userRepo.findById(id)
                .orElseThrow(() -> {
                    throw new CustomException(errMsg, HttpStatus.NOT_FOUND);
                });
    }


    @Override
    public UserInfoResponse updateUser(Long id, UserInfoRequest request) {

        UserInfoResponse userInfoResponse;

        if (!EmailValidator.getInstance().isValid(request.getEmail())) {
            log.error(errorEmail, id);
        }

        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            return null;
        }

        user.setEmail(StringUtils.isBlank(request.getEmail()) ? user.getEmail() : request.getEmail());
        user.setGender(request.getGender() == null ? user.getGender() : request.getGender());
        user.setAge(user.getAge() == null ? user.getAge() : request.getAge());
        user.setFirstName(StringUtils.isBlank(request.getFirstName()) ? user.getFirstName() : request.getFirstName());
        user.setLastName(StringUtils.isBlank(request.getLastName()) ? user.getLastName() : request.getLastName());
        user.setMiddleName(StringUtils.isBlank(request.getMiddleName()) ? user.getMiddleName() : request.getMiddleName());
        user.setPassword(StringUtils.isBlank(request.getPassword()) ? user.getPassword() : request.getPassword());

        user.setStatus(UserStatus.UPDATED);
        user.setUpdatedAt(LocalDateTime.now());

        User save = userRepo.save(user);
        return mapper.convertValue(save, UserInfoResponse.class);
    }


    @Override
    public void deleteUser(Long id) {

        User user = userRepo.findById(id).orElse(null);

        if (user != null) {
            user.setStatus(UserStatus.DELETED);
            user.setUpdatedAt(LocalDateTime.now());
            userRepo.save(user);
        }
    }

    @Override
    public List<UserInfoResponse> getAllUsers() {
        return userRepo.findAll().stream()
                .filter(u -> u.getStatus() != UserStatus.DELETED)
                .map(u -> mapper.convertValue(u, UserInfoResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public User updateCarList(User user) {
        return userRepo.save(user);
    }

    @Override
    public List<CarInfoResponse> getCarsByUser(Long id) {

        User user = getUser(id);
        return user.getCars().stream()
                .map(c -> mapper.convertValue(c, CarInfoResponse.class))
                .collect(Collectors.toList());
    }

    public User find(String firstName) {
        if (StringUtils.isBlank(firstName)) {
            log.error("Invalid firstName", firstName);
        }
        return userRepo.findFirstName(firstName);
    }

    @Override
    public Page<UserInfoResponse> getAllUsers(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable request = getPageRequest(page, perPage, sort, order);

        List<User> pageList;

        if (StringUtils.isBlank(filter)) {
            pageList = userRepo.findAllAdult(request);
        } else {
            pageList = userRepo.findAllAdult(request, filter);
        }

        List<UserInfoResponse> response = pageList.stream()
                .map(u -> mapper.convertValue(u, UserInfoResponse.class))
                .collect(Collectors.toList());


        return new PageImpl<>(response);
    }
}
