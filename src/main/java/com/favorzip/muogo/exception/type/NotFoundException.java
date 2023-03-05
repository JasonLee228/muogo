package com.favorzip.muogo.exception.type;

import com.favorzip.muogo.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * HttpStatus 가 NOT_FOUND 인 예외들의 부모 클래스.
 * status 404
 */
@Getter
public class NotFoundException extends BaseException {

    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    /**
     * NotFoundException 생성자.
     */
    public NotFoundException() {
    }

}
