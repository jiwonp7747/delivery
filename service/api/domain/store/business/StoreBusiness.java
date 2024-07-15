package org.delivery.api.domain.store.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.anotation.Business;
import org.delivery.api.domain.store.controller.model.StoreRegisterRequest;
import org.delivery.api.domain.store.controller.model.StoreResponse;
import org.delivery.api.domain.store.converter.StoreConverter;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.db.store.enums.StoreCategory;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Business
public class StoreBusiness {

    private final StoreService storeService;
    private final StoreConverter storeConverter;

    public StoreResponse register(
            StoreRegisterRequest registerRequest
    ) {
        //req -> entity -> response
        var entity=storeConverter.toEntity(registerRequest);
        var savedEntity=storeService.register(entity);
        var response=storeConverter.toResponse(savedEntity);

        return response;
    }

    public List<StoreResponse> searchCategory(
            StoreCategory storeCategory
    ){

        // entity list-> response list
        var storeList=storeService.searchByCatergory(storeCategory);

        return storeList.stream()
                .map(it->storeConverter.toResponse(it))
                .collect(Collectors.toList());
    }
}
