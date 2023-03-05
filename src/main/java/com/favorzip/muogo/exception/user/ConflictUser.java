package com.favorzip.muogo.exception.user;

import com.favorzip.muogo.exception.type.ForbiddenException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * User 도메인에서 CONFLICT_USER 예외를 어떻게 보여줄건지 정의한 클래스.
 * <p>
 * 권한을 가지고 있지 않은 요청을 할때 보여주는 에러.
 */
@Getter
public class ConflictUser extends ForbiddenException {

    private final String errorCode = "CONFLICT_USER";
    private final String message = "이미 존재하는 회원입니다.";
    private HttpStatus httpStatus;

    /**
     * ConflictUser 생성자.
     */
    public ConflictUser() {
        super();
        this.httpStatus = super.getHttpStatus();
    }
}
