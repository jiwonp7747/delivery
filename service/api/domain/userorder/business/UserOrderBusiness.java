package org.delivery.api.domain.userorder.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.anotation.Business;
import org.delivery.api.domain.store.converter.StoreConverter;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.api.domain.storemenu.conveter.StoreMenuConveter;
import org.delivery.api.domain.storemenu.service.StoreMenuService;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.userorder.controller.model.UserOrderDetailResponse;
import org.delivery.api.domain.userorder.controller.model.UserOrderRequest;
import org.delivery.api.domain.userorder.controller.model.UserOrderResponse;
import org.delivery.api.domain.userorder.converter.UserOrderConverter;
import org.delivery.api.domain.userorder.service.UserOrderService;
import org.delivery.api.domain.userordermenu.converter.UserOrderMenuConverter;
import org.delivery.api.domain.userordermenu.service.UserOrderMenuService;
import org.delivery.db.userorder.UserOrderEntity;

import java.util.List;
import java.util.stream.Collectors;

@Business
@RequiredArgsConstructor
public class UserOrderBusiness {

    private final UserOrderService userOrderService;
    private final StoreMenuService storeMenuService;
    private final StoreMenuConveter storeMenuConveter;
    private final UserOrderConverter userOrderConverter;
    private final UserOrderMenuConverter userOrderMenuConverter;
    private final UserOrderMenuService userOrderMenuService;
    private final StoreService storeService;
    private final StoreConverter storeConverter;
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

    // 현재 진행 중인 주문 내역 가져오기
    // UserOrder, UserOrderMenu, StoreMenu
    public List<UserOrderDetailResponse> current(User user) {

        var userOrderEntityList= userOrderService.current(user.getId());

        // 주문 한 건씩 처리
        var userOrderDetailResponseList=userOrderEntityList.stream().map(it->{

            //사용자가 주문한
            var userOrderMenuEntityList=userOrderMenuService.getUserOrderMenu(it.getId());
            var storeMenuEntityList=userOrderMenuEntityList.stream()
                    .map(userOrderMenuEntity->{
                        return storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                    }).collect(Collectors.toList());


            // 사용자가 주문한 스토어 // TODO 리팩토링 필요
            var storeEntity=storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());

            return UserOrderDetailResponse.builder()
                    .userOrderResponse(userOrderConverter.toResponse(it))
                    .storeMenuResponseList(storeMenuConveter.toResponse(storeMenuEntityList))
                    .storeResponse(storeConverter.toResponse(storeEntity))
                    .build()
                    ;

        }).collect(Collectors.toList());

        return userOrderDetailResponseList;
    }

    public List<UserOrderDetailResponse> history(User user) {
        var userOrderEntityList= userOrderService.history(user.getId());

        // 주문 한 건씩 처리
        var userOrderDetailResponseList=userOrderEntityList.stream().map(it->{

            //사용자가 주문한
            var userOrderMenuEntityList=userOrderMenuService.getUserOrderMenu(it.getId());
            var storeMenuEntityList=userOrderMenuEntityList.stream()
                    .map(userOrderMenuEntity->{
                        return storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                    }).collect(Collectors.toList());


            // 사용자가 주문한 스토어 // TODO 리팩토링 필요
            var storeEntity=storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());

            return UserOrderDetailResponse.builder()
                    .userOrderResponse(userOrderConverter.toResponse(it))
                    .storeMenuResponseList(storeMenuConveter.toResponse(storeMenuEntityList))
                    .storeResponse(storeConverter.toResponse(storeEntity))
                    .build()
                    ;

        }).collect(Collectors.toList());

        return userOrderDetailResponseList;
    }

    public UserOrderDetailResponse read(User user, Long orderId) {

        var userOrderEntity=userOrderService.getUserOrderWithoutStatusWithThrow(orderId, user.getId());

        // 사용자가 주문한 메뉴
        var userOrderMenuEntityList=userOrderMenuService.getUserOrderMenu(userOrderEntity.getId());
        var storeMenuEntityList=userOrderMenuEntityList.stream()
                .map(userOrderMenuEntity->{
                    return storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                }).collect(Collectors.toList());

        // 사용자가 주문한 스토어 // TODO 리팩토링 필요
        var storeEntity=storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());

        return UserOrderDetailResponse.builder()
                .userOrderResponse(userOrderConverter.toResponse(userOrderEntity))
                .storeMenuResponseList(storeMenuConveter.toResponse(storeMenuEntityList))
                .storeResponse(storeConverter.toResponse(storeEntity))
                .build()
                ;
    }
}
