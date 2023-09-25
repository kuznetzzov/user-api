package com.flybuilder.userapi.model.db.entity;

import com.flybuilder.userapi.model.enums.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "cars")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String brand;
    String model;
    Color color;
    Integer year;
    @Column(name = "car_type")
    CarType carType;
    Long price;
    @Column(name = "is_new")
    Boolean isNew;
    @Column(name = "created_at")
    LocalDateTime createdAt;
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
    CarStatus status;
}
