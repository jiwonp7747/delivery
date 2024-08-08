package org.delivery.storeadmin.common.anotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service // Service가 달려있기 때문에 스프링에서 감지
public @interface Converter { // 데이터를 변환시켜주는 객체 어노테이션
    @AliasFor(annotation = Service.class)
    String value() default "";
}
