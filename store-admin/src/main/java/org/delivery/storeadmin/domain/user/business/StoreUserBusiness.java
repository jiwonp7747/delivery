package org.delivery.storeadmin.domain.user.business;

import lombok.RequiredArgsConstructor;
import org.delivery.db.store.StoreRepository;
import org.delivery.db.store.enums.StoreStatus;
import org.delivery.db.storeuser.enums.StoreUserStatus;
import org.delivery.storeadmin.common.anotation.Business;
import org.delivery.storeadmin.domain.user.controller.model.StoreUserRegisterRequest;
import org.delivery.storeadmin.domain.user.controller.model.StoreUserResponse;
import org.delivery.storeadmin.domain.user.converter.StoreUserConverter;
import org.delivery.storeadmin.domain.user.service.StoreUserService;

@Business
@RequiredArgsConstructor
public class StoreUserBusiness {

    private final StoreUserConverter storeUserConverter;
    private final StoreUserService storeUserService;
    private final StoreRepository storeRepository; //TODO Service로 변환하기


    public StoreUserResponse register(
            StoreUserRegisterRequest storeUserRegisterRequest

    ) {
        var storeEntity=storeRepository.findFirstByNameAndStatusOrderByIdDesc(storeUserRegisterRequest.getStoreName(), StoreStatus.REGISTERED);

        var entity= storeUserConverter.toEntity(storeUserRegisterRequest, storeEntity.get());

        var newEntity=storeUserService.register(entity);

        var response=storeUserConverter.toResponse(newEntity, storeEntity.get());

        return response;
    }
}
