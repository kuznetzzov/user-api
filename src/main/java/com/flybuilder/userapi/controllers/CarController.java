package com.flybuilder.userapi.controllers;

import com.flybuilder.userapi.model.dto.request.CarInfoRequest;
import com.flybuilder.userapi.model.dto.response.CarInfoResponse;
import com.flybuilder.userapi.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cars")
@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;


    @PostMapping
    @Operation(summary = "Create car")
    public CarInfoResponse createCar(@RequestBody CarInfoRequest request) {
        return carService.createCar(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get car by id")
    public CarInfoResponse getCar(@PathVariable Long id) {
        return carService.getCar(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update car")
    public CarInfoResponse updateCar(@PathVariable Long id, @RequestBody CarInfoRequest request) {
        return carService.updateCar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete car")
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }


    @GetMapping("/all")
    @Operation(summary = "Get all cars")
    public Page<CarInfoResponse> getAllCars (@RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(defaultValue = "10") Integer perPage,
                                             @RequestParam(defaultValue = "brand") String sort,
                                             @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                             @RequestParam(required = false) String filter) {
        return carService.getAllCars(page, perPage, sort, order, filter);
    }


    @PostMapping("/linkCarAndDriver/{userId}/{carId}")
    @Operation(summary = "Get car by user")
    public CarInfoResponse linkCarAndDriver(@PathVariable Long userId, @PathVariable Long carId) {
        return carService.linkCarAndDriver(userId, carId);
    }
}
