package org.delivery.api.domain.storemenu.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.anotation.Business;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuRegisterRequest;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuResponse;
import org.delivery.api.domain.storemenu.conveter.StoreMenuConveter;
import org.delivery.api.domain.storemenu.service.StoreMenuService;

import java.util.List;
import java.util.stream.Collectors;

@Business
@RequiredArgsConstructor
public class StoreMenuBusiness {

    private final StoreMenuService storeMenuService;
    private final StoreMenuConveter storeMenuConveter;

    public StoreMenuResponse register(StoreMenuRegisterRequest request) {
        var entity=storeMenuConveter.toEntity(request);
        var savedEntity=storeMenuService.register(entity);
        return storeMenuConveter.toResponse(savedEntity);
    }

    public List<StoreMenuResponse> search(Long storeId){
        var list=storeMenuService.getStoreMenuByStoreId(storeId);

        return list.stream()
                .map(it->{
                    return storeMenuConveter.toResponse(it);}
                )
                .collect(Collectors.toList());
    }
}
