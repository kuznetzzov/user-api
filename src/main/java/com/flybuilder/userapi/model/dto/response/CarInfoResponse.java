package com.flybuilder.userapi.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flybuilder.userapi.model.dto.request.CarInfoRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarInfoResponse extends CarInfoRequest {

    Long id;
    UserInfoResponse user;

}
