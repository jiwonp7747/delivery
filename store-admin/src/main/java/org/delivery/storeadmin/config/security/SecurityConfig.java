package org.delivery.storeadmin.config.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

@Configuration
@EnableWebSecurity //시큐리티 활성화
public class SecurityConfig {

    private List<String> SWAGGER =List.of(
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    @Bean // filterChain 빈으로 등록
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector handlerMappingIntrospector) throws Exception{
        MvcRequestMatcher.Builder mvcMatcherBuilder=new MvcRequestMatcher.Builder(handlerMappingIntrospector);
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(authz->
                    authz
                            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                            .requestMatchers(SWAGGER.toArray(new String[0])).permitAll() // list를 배열로 넘기는 방법
                            .requestMatchers(mvcMatcherBuilder.pattern("/open-api/**")).permitAll()
                            .anyRequest().authenticated()

                );

        return httpSecurity.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //Hash 방식으로 암호화
        return new BCryptPasswordEncoder();
    }
}
