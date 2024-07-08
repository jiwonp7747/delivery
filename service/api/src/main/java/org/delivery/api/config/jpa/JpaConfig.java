package org.delivery.api.config.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration // 설정임을 알림
@EntityScan(basePackages = "org.delivery.db") // 경로에 있는  스캔하겠다.
@EnableJpaRepositories(basePackages = "org.delivery.db") // 하위에 있는 레포도 빈으로 등록
public class JpaConfig {

}
