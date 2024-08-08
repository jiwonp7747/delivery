package org.delivery.storeadmin.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.delivery.db.storeuser.StoreUserEntity;
import org.delivery.db.storeuser.enums.StoreUserRepository;
import org.delivery.db.storeuser.enums.StoreUserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreUserService {

    private final StoreUserRepository storeUserRepository;
    private final PasswordEncoder passwordEncoder;

    public StoreUserEntity register(
            StoreUserEntity storeUser
    ){
        storeUser.setStatus(StoreUserStatus.REGISTERED);
        storeUser.setPassword(passwordEncoder.encode(storeUser.getPassword()));
        storeUser.setRegisteredAt(LocalDateTime.now());

        return storeUserRepository.save(storeUser);
    }

    public Optional<StoreUserEntity> getRegisterUser(String email) {
        return storeUserRepository.findFirstByEmailAndStatusOrderByIdDesc(email, StoreUserStatus.REGISTERED);
    }
}
