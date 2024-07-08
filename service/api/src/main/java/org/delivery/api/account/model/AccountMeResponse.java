package org.delivery.api.account.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
//@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class) // 공통화 작업 필요
public class AccountMeResponse {

    private String email;

    private String name;

    private LocalDateTime registeredAt;
}
