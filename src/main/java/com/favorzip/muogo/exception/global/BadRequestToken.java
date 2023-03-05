package com.favorzip.muogo.exception.global;

import com.favorzip.muogo.exception.type.BadRequestException;
import org.springframework.http.HttpStatus;

public class BadRequestToken extends BadRequestException {

    private final String errorCode = "BAD_REQUEST_TOKEN";
    private final String message = "올바르지 않은 토큰입니다.";
    private HttpStatus httpStatus;

    /**
     * BadRequestToken 생성자.
     */
    public BadRequestToken() {
        super();
        this.httpStatus = super.getHttpStatus();
    }
}
