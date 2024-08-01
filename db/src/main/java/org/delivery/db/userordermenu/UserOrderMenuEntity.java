package org.delivery.db.userordermenu;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.delivery.db.BaseEntity;
import org.delivery.db.userordermenu.enums.UserOrderMenuStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name="user_order_menu")
public class UserOrderMenuEntity extends BaseEntity {

    @Column(nullable = false) // 테스트할 때 하이버네이트의 자동 테이블 생성 기능을 위해서
    private Long userOrderId; // 1:n

    @Column(nullable = false)
    private Long storeMenuId; // 1:n

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserOrderMenuStatus status;
}
