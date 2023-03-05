package com.favorzip.muogo.exception.type;

import com.favorzip.muogo.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * HttpStatus 가 BAD_REQUEST 인 예외들의 부모 클래스.
 * status 400
 */
@Getter
public class BadRequestException extends BaseException {

    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    /**
     * BadRequestException 생성자.
     */
    public BadRequestException() {
    }
}
