package org.delivery.api.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.error.UserErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.db.user.UserEntity;
import org.delivery.db.user.UserRepository;
import org.delivery.db.user.enums.UserStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/*
* User 도메인 로직 처리 하는 서비스
* */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    //@Transactional // 오류나 데이터베이스 맞지 않는 경우 롤백을 위해
    public UserEntity register(
            UserEntity userEntity
    ){
        return Optional.ofNullable(userEntity)
                .map(it->{
                    userEntity.setStatus(UserStatus.REGISTERED);
                    userEntity.setRegisteredAt(LocalDateTime.now());
                    return userRepository.save(userEntity);
                })
                .orElseThrow(()->new ApiException(ErrorCode.NULL_POINT, "UserEntity Null"));
    }

    public UserEntity login(String email, String password){
        var entity=getUserWithThrow(email, password);
        return entity;
    }

    public UserEntity getUserWithThrow(String email, String password){
        return userRepository.findFirstByEmailAndPasswordAndStatusOrderByIdDesc(email, password, UserStatus.REGISTERED)
                .orElseThrow(()->new ApiException(UserErrorCode.USER_NOT_FOUNT));
    }

    public UserEntity getUserWithThrow(Long userId){
        return userRepository.findFirstByIdAndStatusOrderByIdDesc(userId, UserStatus.REGISTERED)
                .orElseThrow(()->new ApiException(UserErrorCode.USER_NOT_FOUNT));
    }
}
