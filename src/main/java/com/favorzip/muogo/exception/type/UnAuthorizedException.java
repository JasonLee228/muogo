package com.favorzip.muogo.exception.type;

import com.favorzip.muogo.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * HttpStatus 가 UNAUTHORIZED 인 예외들의 부모 클래스.
 * status 401
 */
@Getter
public class UnAuthorizedException extends BaseException {
    private final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    /**
     * UnAuthorizedException 생성자.
     */
    public UnAuthorizedException() {
    }
}
