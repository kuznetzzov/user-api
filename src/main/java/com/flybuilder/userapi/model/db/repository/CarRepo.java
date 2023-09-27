package com.flybuilder.userapi.model.db.repository;

import com.flybuilder.userapi.model.db.entity.Car;
import com.flybuilder.userapi.model.db.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepo extends JpaRepository<Car, Long> {
    @Query("select c from Car c where c.status <> '2'")
    List<Car> findAllNotDeleted(Pageable request);

    @Query("select c from Car c where c.status <> '2' and  (c.brand like %:filter% or c.model like %:filter%)")
    List<Car> findAllNotDeleted(Pageable request, @Param("filter") String filter);
}
