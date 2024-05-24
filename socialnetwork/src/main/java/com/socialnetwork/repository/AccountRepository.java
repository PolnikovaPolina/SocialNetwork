package com.socialnetwork.repository;

import com.socialnetwork.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

//інтерфейс для роботи з сутністю Account
public interface AccountRepository extends JpaRepository<Account, Long> 
{
    Optional<Account> findByUsername(String username);
}

/*
Інтерфейс AccountRepository розширює інтерфейс 
JpaRepository, який надається Spring Data JPA.
Long – це тип даних первинного ключа сутності Account
JpaRepository надає набір стандартних методів для роботи з базою даних
*/
