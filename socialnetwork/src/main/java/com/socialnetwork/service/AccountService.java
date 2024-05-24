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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .roles("USER")
                .build();
    }
}
