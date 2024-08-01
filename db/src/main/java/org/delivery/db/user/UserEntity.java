package org.delivery.db.user;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.delivery.db.BaseEntity;
import org.delivery.db.user.enums.UserStatus;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity // Entity가 붙어있으면 클래스이름을 스네이크 케이스로 바꿔서 데이터베이스와 비교
@Table(name="user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper=true)
public class UserEntity extends BaseEntity {

    @Column(length = 50 , nullable = false)
    private String name;

    @Column(length = 100 , nullable = false)
    private String password;

    @Column(length = 100 , nullable = false)
    private String email;

    @Column(length = 50 , nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(length = 150 , nullable = false)
    private String address;

    private LocalDateTime registeredAt;

    private LocalDateTime unregisteredAt;

    private LocalDateTime lastLoginAt;
}
