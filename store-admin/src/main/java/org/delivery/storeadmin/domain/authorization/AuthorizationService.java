package org.delivery.storeadmin.domain.authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.storeadmin.domain.user.service.StoreUserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationService implements UserDetailsService {

    private final StoreUserService storeUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var storeUserEntity=storeUserService.getRegisterUser(username);

        return storeUserEntity.map(it->{
                    var user= User.builder()
                            .username(it.getEmail())
                            .password(it.getPassword())
                            .roles(it.getRole().toString())
                            .build();

                    log.info(user.toString());
                    return user;
        })
                .orElseThrow(()->new UsernameNotFoundException(username));

    }
}

/*
* 1. 메인에서 사용자가 로그인 시도
* 2. username이 매개변수로 넘어감
* 3. 사용자를 찾고 없으면 예외처리
* 4. 사용자가 있으면 미리 만들어진 객체를 통해서 user를 리턴
* 5. 스프링 시큐리티에서는 UserDetails 인터페이스에서 패스워드를 가져온다.
* 6. 또한 사용자가 입력한 패스워드를 가져온다
* 7. 사용자가 입력한 패스워드를 비크립트로 해싱한다음 패스워드를 비교한다.
*
* */
