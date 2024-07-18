package org.delivery.api.domain.userorder.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOrderRequest {

    // 주문
    // 특정 사용자가 특정 메뉴를 주문
    // 특정 사용자 = 로그인된 세션에 들어있는 사용자
    // 특정 메뉴 id

    @NotNull
    private List<Long> storeMenuIdList; // 사용자는 외부에서 받을 것이기 때문에 주문하는 메뉴만 알면 된다.
}
