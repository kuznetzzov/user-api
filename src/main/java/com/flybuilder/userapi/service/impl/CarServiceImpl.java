package com.flybuilder.userapi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flybuilder.userapi.exceptions.CustomException;
import com.flybuilder.userapi.model.db.entity.Car;
import com.flybuilder.userapi.model.db.entity.User;
import com.flybuilder.userapi.model.db.repository.CarRepo;
import com.flybuilder.userapi.model.dto.request.CarInfoRequest;
import com.flybuilder.userapi.model.dto.response.CarInfoResponse;
import com.flybuilder.userapi.model.dto.response.UserInfoResponse;
import com.flybuilder.userapi.model.enums.CarStatus;
import com.flybuilder.userapi.service.CarService;
import com.flybuilder.userapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class CarServiceImpl implements CarService {

    private final UserService userService;
    private final CarRepo carRepo;
    private final ObjectMapper mapper;

    private final static String errorStr = "Car with id %d not found";
    private final static String errorEmail = "Invalid email";

    @Override
    public CarInfoResponse createCar(CarInfoRequest request) {
        Car car = mapper.convertValue(request, Car.class);
        car.setCreatedAt(LocalDateTime.now());
        car.setStatus(CarStatus.CREATED);
        Car save = carRepo.save(car);
        return mapper.convertValue(save, CarInfoResponse.class);
    }

    @Override
    public CarInfoResponse getCar(Long id) {
        CarInfoResponse carInfoResponse = new CarInfoResponse();
        if (id != 0) {
            Car car = carRepo.findById(id).orElse(new Car());
            carInfoResponse = mapper.convertValue(car, CarInfoResponse.class);
        } else {
            throw new CustomException(errorStr, HttpStatus.NOT_FOUND);
        }
        return carInfoResponse;
    }

    @Override
    public CarInfoResponse updateCar(Long id, CarInfoRequest request) {

        if (id == 0) {
            throw new CustomException(errorStr, HttpStatus.NOT_FOUND);
        }

        Car car = carRepo.findById(id).orElse(null);
        if (car == null) {
            return null;
        }

        car.setBrand(StringUtils.isBlank(request.getBrand()) ? car.getBrand() : request.getBrand());
        car.setModel(StringUtils.isBlank(request.getModel()) ? car.getModel() : request.getModel());
        car.setColor(request.getColor() == null ? car.getColor() : request.getColor());
        car.setYear(request.getYear() == null ? car.getYear() : request.getYear());
        car.setCarType(request.getCarType() == null ? car.getCarType() : request.getCarType());
        car.setPrice(request.getPrice() == null ? car.getPrice() : request.getPrice());
        car.setIsNew(request.getIsNew() == null ? car.getIsNew() : request.getIsNew());

        car.setStatus(CarStatus.UPDATED);
        car.setUpdatedAt(LocalDateTime.now());

        Car save = carRepo.save(car);
        return mapper.convertValue(save, CarInfoResponse.class);
    }

    @Override
    public void deleteCar(Long id) {

        Car car = carRepo.findById(id).orElse(null);

        if (car != null) {
            car.setStatus(CarStatus.DELETED);
            car.setUpdatedAt(LocalDateTime.now());
            carRepo.save(car);
        }
    }

    public Car getCarById(Long id) {
        if (id == 0) {
            throw new CustomException(errorStr, HttpStatus.NOT_FOUND);
        }
        return carRepo.findById(id)
                .orElse(new Car());
    }

    @Override
    public List<CarInfoResponse> getAllCars() {
        return carRepo.findAll().stream()
                .filter(c -> c.getStatus() != CarStatus.DELETED)
                .map(c -> mapper.convertValue(c, CarInfoResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public CarInfoResponse linkCarAndDriver(Long userId, Long carId) {
        if (userId == 0) {
            throw new CustomException("User with id %d not found", HttpStatus.NOT_FOUND);
        }
        if (carId == 0) {
            throw new CustomException(errorStr, HttpStatus.NOT_FOUND);
        }

        Car car = getCarById(carId);
        User user = userService.getUser(userId);

        user.getCars().add(car);
        userService.updateCarList(user);

        car.setUser(user);
        car = carRepo.save(car);

        CarInfoResponse carInfoResponse = mapper.convertValue(car, CarInfoResponse.class);
        UserInfoResponse userInfoResponse = mapper.convertValue(user, UserInfoResponse.class);

        carInfoResponse.setUser(userInfoResponse);
        return carInfoResponse;
    }

    @Override
    public Page<CarInfoResponse> getAllCars(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable request = getPageRequest(page, perPage, sort, order);

        List<Car> pageList;

        if (StringUtils.isBlank(filter)) {
            pageList = carRepo.findAllNotDeleted(request);
        } else {
            pageList = carRepo.findAllNotDeleted(request, filter);
        }

        List<CarInfoResponse> response = pageList.stream()
                .map(c -> mapper.convertValue(c, CarInfoResponse.class))
                .collect(Collectors.toList());


        return new PageImpl<>(response);
    }
}
