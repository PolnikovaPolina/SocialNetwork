package com.socialnetwork.controller;
//Використовуємо REST контролери для обробки HTTP запитів.
import com.socialnetwork.model.Account;
import com.socialnetwork.model.Publication;
import com.socialnetwork.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Метод для отримання всіх акаунтів
    @GetMapping // Вказує, що цей метод обробляє GET-запити за адресою /api/accounts
    public ResponseEntity<List<Account>> getAllAccounts() {
        // Викликає метод сервісу для отримання списку всіх акаунтів
        List<Account> accounts = accountService.getAllAccounts();
        // Повертає список акаунтів у вигляді HTTP-відповіді з кодом 200 OK
        return ResponseEntity.ok(accounts);
    }

    // Метод для додавання публікації до акаунта
    @PostMapping("/{accountId}/publications") // Вказує, що цей метод обробляє POST-запити за адресою /api/accounts/{accountId}/publications
    public ResponseEntity<Publication> addPublication(@PathVariable Long accountId, @RequestBody Publication publication) {
        // Викликає метод сервісу для додавання публікації до акаунта
        Publication savedPublication = accountService.addPublication(accountId, publication);
        // Перевіряє, чи публікація була успішно збережена
        if (savedPublication != null) {
            // Повертає збережену публікацію у вигляді HTTP-відповіді з кодом 200 OK
            return ResponseEntity.ok(savedPublication);
        } else {
            // Якщо акаунт не знайдений, повертає відповідь з кодом 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

    // Метод для отримання всіх публікацій акаунта за його ID
    @GetMapping("/{accountId}/publications") // Вказує, що цей метод обробляє GET-запити за адресою /api/accounts/{accountId}/publications
    public ResponseEntity<List<Publication>> getPublicationsByAccountId(@PathVariable Long accountId) {
        // Викликає метод сервісу для отримання списку всіх публікацій акаунта за його ID
        List<Publication> publications = accountService.getPublicationsByAccountId(accountId);
        // Повертає список публікацій у вигляді HTTP-відповіді з кодом 200 OK
        return ResponseEntity.ok(publications);
    }
}
