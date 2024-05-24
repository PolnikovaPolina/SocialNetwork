package com.socialnetwork.controller;
//Використовуємо REST контролери для обробки HTTP запитів.
import com.socialnetwork.model.Account;
import com.socialnetwork.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController // Вказує, що цей клас є REST-контролером, який обробляє HTTP-запити
@RequestMapping("/api/accounts") // Вказує базовий URL для всіх методів цього контролера
public class AccountController {

    @Autowired // Впроваджує залежність до AccountService
    private AccountService accountService;

    @Autowired // Впроваджує залежність до AuthenticationManager
    private AuthenticationManager authenticationManager;

    // Метод для реєстрації нового користувача
    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account) {
        // Викликає метод сервісу для реєстрації нового користувача і повертає об'єкт Account
        return ResponseEntity.ok(accountService.registerAccount(account));
    }

    // Метод для входу користувача
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Account account) {
        // Перевіряє автентифікацію користувача за допомогою сервісу
        boolean isAuthenticated = accountService.authenticate(account.getUsername(), account.getPassword());
        if (isAuthenticated) {
            // Якщо автентифікація успішна, створює об'єкт Authentication
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword())
            );
            // Встановлює об'єкт Authentication у SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Повертає успішну відповідь
            return ResponseEntity.ok("Login successful");
        } else {
            // Якщо автентифікація неуспішна, повертає відповідь з кодом 401 (Unauthorized)
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}
