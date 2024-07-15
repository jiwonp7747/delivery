package org.delivery.db.store.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StoreStatus {

    REGISTERED("등록"),
    UNREGISTERED("해지"),
    ;

    // ToDo 등록 상태와 해지 상태사이에 "해지 신청" 과 같은 상태 추가

    private String description;
}
