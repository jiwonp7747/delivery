package org.delivery.db.account;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.delivery.db.BaseEntity;

@SuperBuilder // 부모가 가지고 있는 변수가 지정 가능
@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name="account")
@NoArgsConstructor
public class AccountEntity extends BaseEntity {
}
