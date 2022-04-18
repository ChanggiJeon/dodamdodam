package com.ssafy.dodamdodam.config;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
@NoArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    private final JwtProvider

    protected void configure(HttpSecurity http) throws Exception {
        // basic authentication
        http
            .httpBasic().disable() // BasicAuthenticationFilter 비활성화
            .csrf().disable() // CsrfFilter 비활성화
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS); //토큰 기반 인증(세션 X)

        // jwt filter
        http.addFilterBefore(
                new JwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class
        ).addFilterBefore(
                new JwtAuthorizationFilter(),
                BasicAuthenticationFilter.class
        );

        // authorization
        http.authorizeRequests()
                // /와 /home, /room의 참가, 닉네임 중복확인은 모두에게 허용
                .antMatchers("/", "/home", "/user/signup").permitAll()
                .anyRequest().authenticated();

    }



    @Override
    public void configure(WebSecurity web) throws Exception {
        // static 디렉터리의 하위 파일 목록은 인증 무시 ( = 항상통과 )
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**");
        web.ignoring().antMatchers("/api/user/social", "/api/user/refresh");
    }

}
