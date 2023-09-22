package com.flybuilder.userapi.service.impl;

import com.flybuilder.userapi.model.dto.request.CarInfoRequest;
import com.flybuilder.userapi.model.dto.response.CarInfoResponse;
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
public class CarServiceImpl implements CarService {

    private final UserService userService;
    private List<CarInfoResponse> cars = new ArrayList<>();

    @Override
    public CarInfoResponse createCar(CarInfoRequest request) {
        log.warn("Car created!");
        CarInfoResponse car = CarInfoResponse.builder()
                .id(100L)
                .brand(request.getBrand())
                .model(request.getModel())
                .color(request.getColor())
                .year(request.getYear())
                .carType(request.getCarType())
                .price(request.getPrice())
                .isNew(request.getIsNew())
                .build();
        cars.add(car);
        return car;
    }

    @Override
    public CarInfoResponse getCar(Long id) {
        CarInfoResponse carInfoResponse = new CarInfoResponse();
        for (CarInfoResponse car : cars) {
            if (Objects.equals(id, car.getId())){
                carInfoResponse = car;
            } else {
                log.error("Car not found");
            }
        }
        return carInfoResponse;
    }

    @Override
    public CarInfoResponse updateCar(Long id, CarInfoRequest request) {
        return null;
    }

    @Override
    public void deleteCar(Long id) {

    }

    @Override
    public List<CarInfoResponse> getAllCars() {
        return null;
    }

    @Override
    public CarInfoResponse linkCarAndDriver(Long userId, Long carId) {
        CarInfoResponse car = getCar(carId);
        UserInfoResponse user = userService.getUser(userId);
        car.setUser(user);
        return car;
    }
}
