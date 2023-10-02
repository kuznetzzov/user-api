package com.flybuilder.userapi.model.db.repository;

import com.flybuilder.userapi.model.db.entity.Car;
import com.flybuilder.userapi.model.db.entity.User;
import com.flybuilder.userapi.model.enums.UserStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findAllByStatus(UserStatus status);

    @Query(nativeQuery = true, value = "select * from users where first_name = :firstName order by first_name desc limit 1")
    User findFirstName(@Param("firstName") String firstName);

    @Query("select u from  User u where u.firstName = :firstName")
    List<User> findAllFirstName(@Param("firstName") String firstName);

    @Query("From User where age > 18 and age < 127")
    List<User> findAllAdult(Pageable request);

    @Query("From User where age > 18 and gender like :filter%")
    List<User> findAllAdult(Pageable request, @Param("filter") String filter);

}
