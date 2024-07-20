package org.delivery.api.domain.userorder.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.anotation.Business;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.exception.ApiException;
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
                .map(storeMenuService::getStoreMenuWithThrow)
                .collect(Collectors.toList());

        var userOrderEntity = userOrderConverter.toEntity(user, storeMenuEntityList);

        //주문
        var newUserOrderEntity=userOrderService.order(userOrderEntity);

        //매핑
        var userOrderMenuEntityList=storeMenuEntityList.stream()
                .map(it->{return userOrderMenuConverter.toEntity(newUserOrderEntity, it);})
                .toList();

        // 주문 내역 기록 남기기
        userOrderMenuEntityList.forEach(userOrderMenuService::order);

        //응답
        return userOrderConverter.toResponse(newUserOrderEntity);
    }

    // 현재 진행 중인 주문 내역 가져오기
    // UserOrder, UserOrderMenu, StoreMenu
    public List<UserOrderDetailResponse> current(User user) {

        // 1. 현재 진행 중인 오더 리스트
        var userOrderEntityList= userOrderService.current(user.getId());

        return toDetail(userOrderEntityList);
        /*// 주문 한 건씩 처리

        return userOrderEntityList.stream().map(it->{

            //사용자가 주문한
            var userOrderMenuEntityList=userOrderMenuService.getUserOrderMenu(it.getId()); // 오더에 해당하는 메뉴리스트
            var storeMenuEntityList=userOrderMenuEntityList.stream() //2. 오더메뉴를 통해서 스토어 메뉴 storeMenuId -> id
                    .map(userOrderMenuEntity->{
                        return storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                    }).collect(Collectors.toList());


            var storeMenuEntity=storeMenuEntityList.stream().findFirst().orElseThrow(()->new ApiException(ErrorCode.NULL_POINT));
            var storeEntity=storeService.getStoreWithThrow(storeMenuEntity.getStoreId());


            return UserOrderDetailResponse.builder()
                    .userOrderResponse(userOrderConverter.toResponse(it))
                    .storeMenuResponseList(storeMenuConveter.toResponse(storeMenuEntityList))
                    .storeResponse(storeConverter.toResponse(storeEntity))
                    .build()
                    ;

        }).collect(Collectors.toList());*/
    }

    public List<UserOrderDetailResponse> history(User user) {

        var userOrderEntityList=userOrderService.history(user.getId());

        // UserOrderMenu -> StoreMenu -> Store

        return userOrderEntityList.stream().map(it->{
            // 한개의 UserOrder에 대한 메뉴 리스트
            var userOrderMenuEntityList=userOrderMenuService.getUserOrderMenu(it.getId());

            // UserOrderMenuEntity의 storeMenuId를 통해서 StoreMenuEntity를 찾는다.
            var storeMenuEntityList=userOrderMenuEntityList.stream().map(userOrderMenuEntity->
                storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId())
            ).collect(Collectors.toList());


            var storeMenuEntity=storeMenuEntityList.stream().findFirst().orElseThrow(()->new ApiException(ErrorCode.NULL_POINT));
            var storeEntity=storeService.getStoreWithThrow(storeMenuEntity.getStoreId());

            return UserOrderDetailResponse.builder()
                    .userOrderResponse(userOrderConverter.toResponse(it))
                    .storeMenuResponseList(storeMenuConveter.toResponse(storeMenuEntityList))
                    .storeResponse(storeConverter.toResponse(storeEntity))
                    .build()
                    ;
        }).collect(Collectors.toList());
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

    //ToDo 공통된 코드 메소드로 빼기 올바른 분리는 아니지만 연습삼아 해보기
    public List<UserOrderDetailResponse> toDetail(List<UserOrderEntity> userOrderEntityList){

        return userOrderEntityList.stream().map(it->{
            // 한개의 UserOrder에 대한 메뉴 리스트
            var userOrderMenuEntityList=userOrderMenuService.getUserOrderMenu(it.getId());

            // UserOrderMenuEntity의 storeMenuId를 통해서 StoreMenuEntity를 찾는다.
            var storeMenuEntityList=userOrderMenuEntityList.stream().map(userOrderMenuEntity->
                    storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId())
            ).collect(Collectors.toList());


            var storeMenuEntity=storeMenuEntityList.stream().findFirst().orElseThrow(()->new ApiException(ErrorCode.NULL_POINT));
            var storeEntity=storeService.getStoreWithThrow(storeMenuEntity.getStoreId());

            return UserOrderDetailResponse.builder()
                    .userOrderResponse(userOrderConverter.toResponse(it))
                    .storeMenuResponseList(storeMenuConveter.toResponse(storeMenuEntityList))
                    .storeResponse(storeConverter.toResponse(storeEntity))
                    .build()
                    ;
        }).collect(Collectors.toList());
    }
}
