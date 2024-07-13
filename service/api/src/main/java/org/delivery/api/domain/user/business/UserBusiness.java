package org.delivery.api.domain.user.business;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.delivery.api.common.anotation.Business;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.token.business.TokenBusiness;
import org.delivery.api.domain.token.controller.model.TokenResponse;
import org.delivery.api.domain.user.controller.model.UserLoginRequest;
import org.delivery.api.domain.user.controller.model.UserRegisterRequest;
import org.delivery.api.domain.user.controller.model.UserResponse;
import org.delivery.api.domain.user.converter.UserConverter;
import org.delivery.api.domain.user.service.UserService;

import java.util.Optional;

@RequiredArgsConstructor
@Business
public class UserBusiness { // Business는 동레벨의 business 언제나 가져다 쓸 수 있다.

    private final UserService userService;
    private final UserConverter userConverter;
    private final TokenBusiness tokenBusiness;
    /* 사용자에 대한 가입처리 로직
        1. request -> entity
        2. entity->save
        3. save-> response
        4. response-> return

    * */
    public UserResponse register(UserRegisterRequest request) {
        var entity=userConverter.toEntity(request);
        var newEntity=userService.register(entity);
        var response=userConverter.toResponse(newEntity);
        return response;

        /*return Optional.ofNullable(request)
                .map(userConverter::toEntity)
                .map(userService::register)
                .map(userConverter::toResponse)
                .orElseThrow(()->new ApiException(ErrorCode.NULL_POINT, "Request Null"));*/
    }

    /*
    * 1.  email과 password를 가지고 사용자 체크
    * 2. user entity 로그인 확인
    * 3. token 생성
    * 4. token response
    * */
    public TokenResponse login(UserLoginRequest request) {
        var userEntity=userService.login(request.getEmail(), request.getPassword());
        // 사용자 없으면 throw

        // 사용자 있으면

        // TODO 토큰 생성 로직으로 변경하기
        var tokenResponse=tokenBusiness.issueToken(userEntity);

//        return userConverter.toResponse(userEntity);
        return tokenResponse;
    }
}
