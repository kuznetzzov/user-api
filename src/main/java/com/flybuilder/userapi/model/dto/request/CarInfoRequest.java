package com.flybuilder.userapi.model.dto.request;

import com.flybuilder.userapi.model.enums.CarType;
import com.flybuilder.userapi.model.enums.Color;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarInfoRequest {

    String brand;
    String model;
    Color color;
    Integer year;
    CarType carType;
    Long price;
    Boolean isNew;

}
