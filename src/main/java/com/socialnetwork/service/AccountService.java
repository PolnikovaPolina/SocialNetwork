package com.socialnetwork.service;

import com.socialnetwork.model.Account;
import com.socialnetwork.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

@Service // Позначає клас як сервісний компонент Spring, який містить бізнес-логіку
public class AccountService implements UserDetailsService {
    
    @Autowired // Автоматичне впровадження залежності AccountRepository
    private AccountRepository accountRepository;

    @Autowired // Автоматичне впровадження залежності BCryptPasswordEncoder для шифрування паролів
    private BCryptPasswordEncoder passwordEncoder;

    // Метод для реєстрації нового облікового запису
    public Account registerAccount(Account account) {
        // Шифрує пароль перед збереженням
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        // Зберігає обліковий запис у базі даних і повертає збережений об'єкт
        return accountRepository.save(account);
    }

    // Метод для пошуку облікового запису за ім'ям користувача
    public Optional<Account> findByUsername(String username) {
        // Використовує метод репозиторію для пошуку облікового запису
        return accountRepository.findByUsername(username);
    }

    //авторизує користувача
    public boolean authenticate(String username, String password) {
        // Шукає обліковий запис за ім'ям користувача
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        
        // Перевіряє, чи знайдено обліковий запис
        if (accountOpt.isPresent()) {
            // Отримує обліковий запис з Optional
            Account account = accountOpt.get();
            // Перевіряє, чи збігається наданий пароль з зашифрованим паролем облікового запису
            return passwordEncoder.matches(password, account.getPassword());
        }
        
        // Повертає false, якщо обліковий запис не знайдено або паролі не збігаються
        return false;
    }

    @Override // Вказує, що цей метод перевизначає метод з інтерфейсу UserDetailsService
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Використовуємо accountRepository для пошуку облікового запису за ім'ям користувача
    Account account = accountRepository.findByUsername(username)
            // Якщо користувача з таким ім'ям не знайдено, кидаємо виняток UsernameNotFoundException
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    
    // Створюємо і повертаємо об'єкт User, який імплементує інтерфейс UserDetails
    return User.builder()
            // Встановлюємо ім'я користувача
            .username(account.getUsername())
            // Встановлюємо пароль (хешований)
            .password(account.getPassword())
            // Встановлюємо роль користувача (в даному випадку "USER")
            .roles("USER")
            // Завершуємо побудову об'єкта User
            .build();
}
}
