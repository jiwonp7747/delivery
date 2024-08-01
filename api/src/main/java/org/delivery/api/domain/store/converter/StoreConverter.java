package org.delivery.api.domain.store.converter;

import org.delivery.api.common.anotation.Converter;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.store.controller.model.StoreRegisterRequest;
import org.delivery.db.store.StoreEntity;
import org.delivery.api.domain.store.controller.model.StoreResponse;

import java.util.Optional;

@Converter
public class StoreConverter {

    public StoreEntity toEntity(
            StoreRegisterRequest request
    ) {
        return Optional.ofNullable(request)
                .map(it->{
                    return StoreEntity.builder()
                            .name(request.getName())
                            .address(request.getAddress())
                            .category(request.getStoreCategory())
                            .thumbnailUrl(request.getThumbnailUrl())
                            .minimumAmount(request.getMinimumAmount())
                            .minimumDeliveryAmount(request.getMinimumDeliveryAmount())
                            .phoneNumber(request.getPhoneNumber())
                            .build()
                            ;
                })
                .orElseThrow(()->new ApiException(ErrorCode.NULL_POINT)); //orElseThorw는 함수형 인터페이스를 받는다.


    }

    public StoreResponse toResponse(
            StoreEntity entity
    ){
        return Optional.ofNullable(entity)
                .map(it->{
                    return StoreResponse.builder()
                            .id(entity.getId())
                            .name(entity.getName())
                            .address(entity.getAddress())
                            .category(entity.getCategory())
                            .status(entity.getStatus())
                            .thumbnailUrl(entity.getThumbnailUrl())
                            .minimumAmount(entity.getMinimumAmount())
                            .minimumDeliveryAmount(entity.getMinimumDeliveryAmount())
                            .phoneNumber(entity.getPhoneNumber())
                            .star(entity.getStar())
                            .build();
                })
                .orElseThrow(()->new ApiException(ErrorCode.NULL_POINT));

    }
}
