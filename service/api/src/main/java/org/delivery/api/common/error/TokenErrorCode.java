package org.delivery.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
* Token 경우 2000번대 에러코드 사용*/
@AllArgsConstructor
@Getter
public enum TokenErrorCode implements ErrorCodeIfs{

    INVALID_TOKEN(400, 2000, "유요하지 않은 토큰 "),
    EXPIRED_TOKEN(400, 2001, "만료된 토큰 "),

    TOKEN_EXCEPTION(400, 2002, "토큰 알 수 없는 에러")
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
