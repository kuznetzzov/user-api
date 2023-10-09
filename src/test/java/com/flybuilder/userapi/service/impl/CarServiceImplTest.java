package com.flybuilder.userapi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flybuilder.userapi.exceptions.CustomException;
import com.flybuilder.userapi.model.db.entity.Car;
import com.flybuilder.userapi.model.db.entity.User;
import com.flybuilder.userapi.model.db.repository.CarRepo;
import com.flybuilder.userapi.model.db.repository.UserRepo;
import com.flybuilder.userapi.model.dto.request.CarInfoRequest;
import com.flybuilder.userapi.model.dto.request.UserInfoRequest;
import com.flybuilder.userapi.model.dto.response.CarInfoResponse;
import com.flybuilder.userapi.model.dto.response.UserInfoResponse;
import com.flybuilder.userapi.model.enums.*;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.flybuilder.userapi.utils.PaginationUtil.getPageRequest;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Disabled
@RunWith(MockitoJUnitRunner.class)
public class CarServiceImplTest {

    @InjectMocks
    private CarServiceImpl carService;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private CarRepo carRepo;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void testCreateCar() {
        CarInfoRequest request = new CarInfoRequest();

        Car car = new Car();
        car.setId(1L);
        when(carRepo.save(any(Car.class))).thenReturn(car);

        CarInfoResponse result = carService.createCar(request);
        assertEquals(Optional.of(car.getId()), Optional.of(result.getId()));
    }

    @Test
    public void getCarWithValidId() {
        Long carId = 1L;
        Car mockCar = new Car();
        when(carRepo.findById(carId)).thenReturn(Optional.of(mockCar));

        CarInfoResponse mockResponse = new CarInfoResponse();
        when(mapper.convertValue(mockCar, CarInfoResponse.class)).thenReturn(mockResponse);

        CarInfoResponse result = carService.getCar(carId);
        assertEquals(mockResponse, result);
    }


    @Test(expected = CustomException.class)
    public void getCarWithInvalidId() {
        Long carId = 0L;
        carService.getCar(carId);
    }

    @Test
    public void updateCar() {
        CarInfoRequest request = new CarInfoRequest();
        request.setColor(Color.GREEN);

        Car car = new Car();
        car.setId(1L);
        car.setBrand("Honda");
        car.setModel("Accord");
        car.setCarType(CarType.SEDAN);
        car.setColor(Color.BLACK);

        when(carRepo.findById(car.getId())).thenReturn(Optional.of(car));

        when(carRepo.save(any(Car.class))).thenReturn(car);

        CarInfoResponse result = carService.updateCar(car.getId(), request);

        assertEquals(request.getColor(), result.getColor());
        assertEquals(car.getBrand(), result.getBrand());
    }

    @Test(expected = CustomException.class)
    public void updateCarWithInvalidId() {
        Long carId = 0L;
        carService.getCar(carId);
    }

    @Test
    public void deleteCar() {
        Car car = new Car();
        car.setId(1L);

        when(carRepo.findById(1L)).thenReturn(Optional.of(car));

        carService.deleteCar(1L);
        verify(carRepo, times(1)).save(any(Car.class));
        assertEquals(CarStatus.DELETED, car.getStatus());
    }

    @Test(expected = CustomException.class)
    public void deleteCarWithInvalidId() {
        Long carId = 0L;
        carService.getCarById(carId);
    }

    @Test
    public void testGetCarByIdWithValidId() {
        Long carId = 1L;
        Car mockCar = new Car();
        when(carRepo.findById(carId)).thenReturn(Optional.of(mockCar));

        Car result = carService.getCarById(carId);

        assertNotNull(result);
    }

    @Test(expected = CustomException.class)
    public void testGetCarByIdWithInvalidId() {
        Long carId = 0L;

        carService.getCarById(carId);
    }

    @Test
    public void testGetCarByIdWithNonExistentId() {
        Long carId = 2L;
        when(carRepo.findById(carId)).thenReturn(Optional.empty()); // Мокируем вызов carRepo
        Car result = carService.getCarById(carId);
        assertNotNull(result);
    }

    @Test
    public void testGetAllCars() {
        Car car1 = new Car();
        car1.setId(1L);
        car1.setStatus(CarStatus.CREATED);

        Car car2 = new Car();
        car2.setId(2L);
        car2.setStatus(CarStatus.DELETED);

        when(carRepo.findAll()).thenReturn(Arrays.asList(car1, car2));
        List<CarInfoResponse> resultCars = carService.getAllCars();
        assertNotNull(resultCars);

        assertEquals(1, resultCars.size());
    }

    @Test
    public void testLinkCarAndDriver() {

    }

    @Test
    public void testGetAllCarsWithFilter() {

        Integer page = 0;
        Integer perPage = 10;
        String sort = "brand";
        Sort.Direction order = Sort.Direction.ASC;
        String filter = "Ford";

        Page<CarInfoResponse> result = carService.getAllCars(page, perPage, sort, order, filter);

        assertNotNull(result);
    }

}