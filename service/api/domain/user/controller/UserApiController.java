package org.delivery.api.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.anotation.UserSession;
import org.delivery.api.common.api.Api;
import org.delivery.api.domain.user.business.UserBusiness;
import org.delivery.api.domain.user.controller.model.UserResponse;
import org.delivery.api.domain.user.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user") //Controller -> Business -> Service -> Repository
public class UserApiController { // 로그인된 사용자에 대해 처리하는 controller

    private final UserBusiness userBusiness;

    @GetMapping("/me")
    public Api<UserResponse> me(
            @UserSession User user
    ){ // 로그인 했을 때 나의 정보를 가져가는 코드

        //reuqestContext는 request가 들어올 때마다 생성
        // reqest가 filter -> interceptor -> controller를 돌고 response로 나갈 때까지 유지되는 스레드 로컬
        var response = userBusiness.me(user);
        return Api.OK(response);
    }
}
