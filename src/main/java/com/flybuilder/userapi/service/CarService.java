package com.flybuilder.userapi.service;

import com.flybuilder.userapi.model.dto.request.CarInfoRequest;
import com.flybuilder.userapi.model.dto.response.CarInfoResponse;

import java.util.List;

public interface CarService {

    CarInfoResponse createCar(CarInfoRequest request);

    CarInfoResponse getCar(Long id);

    CarInfoResponse updateCar(Long id, CarInfoRequest request);

    void deleteCar(Long id);

    List<CarInfoResponse> getAllCars();

    CarInfoResponse linkCarAndDriver(Long userId, Long carId);
}
