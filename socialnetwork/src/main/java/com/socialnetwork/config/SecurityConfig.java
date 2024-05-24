package com.socialnetwork.config;

import com.socialnetwork.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Вказує, що цей клас є конфігураційним класом Spring
@EnableWebSecurity // Включає підтримку безпеки у веб-додатку Spring
public class SecurityConfig {

    @Autowired // Впроваджує залежність до AccountService
    private AccountService accountService;

    @Bean // Вказує, що цей метод створює бін, керований Spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Вимикає захист від CSRF (Cross-Site Request Forgery)
            .authorizeRequests()
                // Дозволяє незахищений доступ до URL /api/accounts/register і /api/accounts/login
                .requestMatchers("/api/accounts/register", "/api/accounts/login").permitAll()
                // Вимагає автентифікації для всіх інших запитів
                .anyRequest().authenticated()
            .and()
            // Дозволяє всім доступ до форми входу
            .formLogin().permitAll()
            .and()
            // Дозволяє всім доступ до функції виходу з системи
            .logout().permitAll();

        // Повертає налаштований об'єкт SecurityFilterChain
        return http.build();
    }

    @Bean // Вказує, що цей метод створює бін, керований Spring
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // Отримує спільний об'єкт AuthenticationManagerBuilder
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        // Налаштовує AuthenticationManagerBuilder для використання accountService і passwordEncoder
        authenticationManagerBuilder.userDetailsService(accountService).passwordEncoder(passwordEncoder());
        // Повертає налаштований об'єкт AuthenticationManager
        return authenticationManagerBuilder.build();
    }

    @Bean // Вказує, що цей метод створює бін, керований Spring
    public PasswordEncoder passwordEncoder() {
        // Створює та повертає об'єкт BCryptPasswordEncoder для кодування паролів
        return new BCryptPasswordEncoder();
    }
}

