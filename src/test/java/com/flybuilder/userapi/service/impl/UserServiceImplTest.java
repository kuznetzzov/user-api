package com.flybuilder.userapi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flybuilder.userapi.exceptions.CustomException;
import com.flybuilder.userapi.model.db.entity.Car;
import com.flybuilder.userapi.model.db.entity.User;
import com.flybuilder.userapi.model.db.repository.UserRepo;
import com.flybuilder.userapi.model.dto.request.UserInfoRequest;
import com.flybuilder.userapi.model.dto.response.CarInfoResponse;
import com.flybuilder.userapi.model.dto.response.UserInfoResponse;
import com.flybuilder.userapi.model.enums.Gender;
import com.flybuilder.userapi.model.enums.UserStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Disabled
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepo userRepo;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createUser() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@test.com");

        User user = new User();
        user.setId(1L);

        when(userRepo.save(any(User.class))).thenReturn(user);

        UserInfoResponse result = userService.createUser(request);
        assertEquals(Optional.of(user.getId()), Optional.of(result.getId()));
    }

    @Test(expected = CustomException.class)
    public void createUser_badEmail() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@.test.com");

        userService.createUser(request);
    }

    @Test(expected = CustomException.class)
    public void createUser_userExists() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@test.com");

        User user = new User();
        user.setId(1L);
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));

        userService.createUser(request);
    }

    @Test(expected = CustomException.class)
    public void testGetUserDtoWithInvalidId() {
        Long userId = 0L;
        userService.getUserDto(userId);
    }


    @Test
    public void testGetUserWhenUserExists() {
        User mockUser = new User();
        mockUser.setId(1L);

        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));

        User resultUser = userService.getUser(1L);
        assertNotNull(resultUser);

        assertEquals(1L, resultUser.getId().longValue());
    }

    @Test(expected = CustomException.class)
    public void testGetUserWhenUserDoesNotExist() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        userService.getUser(1L);
    }

    @Test
    public void updateUser() {
        UserInfoRequest request = new UserInfoRequest();
        request.setAge(25);

        User user = new User();
        user.setLastName("Ivanov");
        user.setFirstName("Ivan");
        user.setMiddleName("Ivanovich");
        user.setGender(Gender.MALE);
        user.setPassword("12345");
        user.setAge(35);
        user.setEmail("test@test.com");
        user.setId(1L);

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        when(userRepo.save(any(User.class))).thenReturn(user);

        UserInfoResponse result = userService.updateUser(user.getId(), request);

        assertEquals(request.getAge(), result.getAge());
        assertEquals(user.getFirstName(), result.getFirstName());
    }

    @Test(expected = CustomException.class)
    public void updateUserWithNullUser() {
        Long userId = 123L;
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        User result = userService.getUser(userId);

        assertNull(result);
    }

    @Test
    public void deleteUser() {
        User user = new User();
        user.setId(1L);

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);
        verify(userRepo, times(1)).save(any(User.class));
        assertEquals(UserStatus.DELETED, user.getStatus());
    }

    @Test
    public void testGetAllUsers() {
        // Создаем мок-пользователей
        User user1 = new User();
        user1.setId(1L);
        user1.setStatus(UserStatus.CREATED);

        User user2 = new User();
        user2.setId(2L);
        user2.setStatus(UserStatus.DELETED);

        when(userRepo.findAll()).thenReturn(Arrays.asList(user1, user2));
        List<UserInfoResponse> resultUsers = userService.getAllUsers();
        assertNotNull(resultUsers);

        assertEquals(1, resultUsers.size());
    }

    @Test
    public void testUpdateCarList() {
        User user = new User();
        userService.updateCarList(user);
        verify(userRepo, times(1)).save(user);
    }

    @Test
    public void testGetCarsByUser() {
        User user = new User();
        user.setId(1L);
        user.setStatus(UserStatus.CREATED);

        Car car1 = new Car();
        car1.setId(1L);

        Car car2 = new Car();
        car2.setId(2L);

        user.setCars(Arrays.asList(car1, car2));

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        List<CarInfoResponse> resultCars = userService.getCarsByUser(1L);

        assertNotNull(resultCars);
        assertEquals(2, resultCars.size());
        assertEquals(1L, resultCars.get(0).getId().longValue());
        assertEquals(2L, resultCars.get(1).getId().longValue());
    }

    @Before
    public void setUp() {
        when(userRepo.findFirstName(anyString())).thenReturn(new User());
    }

    @Test
    public void testFind() {
        String firstName = "John";
        User result = userService.find(firstName);
        assertNotNull(result);
    }

    @Test
    public void testFindWithEmptyFirstName() {
        String emptyFirstName = "";
        when(userRepo.findFirstName(emptyFirstName)).thenReturn(null);
        User result = userService.find(emptyFirstName);
        verify(userRepo, times(1)).findFirstName(emptyFirstName);
        assertNull(result);
    }

    @Before
    public void setUpTwo() {
        List<User> mockUserList = new ArrayList<>();
        mockUserList.add(new User());
        when(userRepo.findAllAdult(any(Pageable.class))).thenReturn(mockUserList);
        when(userRepo.findAllAdult(any(Pageable.class), anyString())).thenReturn(mockUserList);
    }

    @Test
    public void testGetAllUsersPage() {
        Integer page = 0;
        Integer perPage = 10;
        String sort = "lastName";
        Sort.Direction order = Sort.Direction.ASC;
        String filter = null;

        Page<UserInfoResponse> result = userService.getAllUsers(page, perPage, sort, order, filter);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testGetAllUsersWithFilter() {
        Integer page = 0;
        Integer perPage = 10;
        String sort = "lastName";
        Sort.Direction order = Sort.Direction.ASC;
        String filter = "John";

        Page<UserInfoResponse> result = userService.getAllUsers(page, perPage, sort, order, filter);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

}