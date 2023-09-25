package com.flybuilder.userapi.model.dto.request;

import com.flybuilder.userapi.model.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfoRequest {

    String email;
    String password;
    String firstName;
    String lastName;
    String middleName;
    Integer age;
    Gender gender;

}
