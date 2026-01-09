package com.helpoldman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // 禁用CSRF保护，便于开发测试
            .authorizeRequests()
                .antMatchers("/api/**").permitAll()  // 允许所有API请求
                .antMatchers("/**").permitAll()      // 允许所有请求
                .anyRequest().authenticated()
            .and()
            .formLogin().disable()  // 禁用表单登录
            .httpBasic().disable(); // 禁用HTTP基本认证

        return http.build();
    }
}