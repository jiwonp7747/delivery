package org.delivery.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
/*
* User의 경우 1000번대 에러코드 사용*/
@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCodeIfs{

    USER_NOT_FOUNT(400, 1404, "사용자를 찾을 수 없음.")

    ;


    private final Integer httpStatusCode;

    private final Integer errorCode; //내부 코드

    private final String description;

    /*@Override
    public Integer getHttpStatusCode() {
        return 0;
    }

    @Override
    public Integer getErrorCode() {
        return 0;
    }

    @Override
    public String getDescription() {
        return "";
    }*/
}
