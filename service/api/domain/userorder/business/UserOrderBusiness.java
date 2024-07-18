package org.delivery.api.domain.userorder.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.anotation.Business;
import org.delivery.api.domain.storemenu.service.StoreMenuService;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.userorder.controller.model.UserOrderRequest;
import org.delivery.api.domain.userorder.controller.model.UserOrderResponse;
import org.delivery.api.domain.userorder.converter.UserOrderConverter;
import org.delivery.api.domain.userorder.service.UserOrderService;
import org.delivery.api.domain.userordermenu.converter.UserOrderMenuConverter;
import org.delivery.api.domain.userordermenu.service.UserOrderMenuService;

import java.util.stream.Collectors;

@Business
@RequiredArgsConstructor
public class UserOrderBusiness {

    private final UserOrderService userOrderService;
    private final StoreMenuService storeMenuService;
    private final UserOrderConverter userOrderConverter;
    private final UserOrderMenuConverter userOrderMenuConverter;
    private final UserOrderMenuService userOrderMenuService;
    // 1. 사용자, 메뉴 id
    // 2. userOrder 생성
    // 3. userOrder 메뉴 생성
    // 4. 응답 생성
    public UserOrderResponse userOrder(User user, UserOrderRequest body) { // 주문하기
        // 메뉴 id 리스트를 가지고 메뉴 entity 리스트 가져오기
        var storeMenuEntityList=body.getStoreMenuIdList()
                .stream()
                .map(it->storeMenuService.getStoreMenuWithThrow(it))
                .collect(Collectors.toList());

        var userOrderEntity = userOrderConverter.toEntity(user, storeMenuEntityList);

        //주문
        var newUserOrderEntity=userOrderService.order(userOrderEntity);

        //매핑
        var userOrderMenuEntityList=storeMenuEntityList.stream()
                .map(it->{return userOrderMenuConverter.toEntity(newUserOrderEntity, it);})
                .collect(Collectors.toList());

        // 주문 내역 기록 남기기
        userOrderMenuEntityList.forEach(userOrderMenuService::order);

        //응답
        return userOrderConverter.toResponse(newUserOrderEntity);
    }
}
