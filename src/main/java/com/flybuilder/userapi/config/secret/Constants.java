package com.flybuilder.userapi.config.secret;

import com.flybuilder.userapi.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public abstract class Constants {

    public static final String API_KEY = "SmF2YSBEZXZlbG9wZXIgZnJvbSBJVE1PIQ==";

    public static void validateKey(String apiKey) {

        if (!apiKey.equals(API_KEY)) {
            throw new CustomException("Неверный ключ", HttpStatus.FORBIDDEN);
        }
    }

}
