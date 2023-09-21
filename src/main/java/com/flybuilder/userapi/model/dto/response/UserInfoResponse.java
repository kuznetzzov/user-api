package com.flybuilder.userapi.model.dto.response;

import com.flybuilder.userapi.model.dto.request.UserInfoRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfoResponse extends UserInfoRequest {

    Long id;

}
