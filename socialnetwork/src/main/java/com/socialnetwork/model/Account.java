package com.socialnetwork.model;

import javax.persistence.*;

@Entity  // Позначає клас як сутність (entity) для JPA
public class Account 
{
    @Id  // Позначає поле як первинний ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Вказує стратегію генерації значень для первинного ключа
    private Long id;

    @Column(name = "username", nullable = false, unique = true)  // Налаштовує відображення поля в стовпець "username"
    private String username;

    @Column(name = "password", nullable = false)  // Налаштовує відображення поля в стовпець "password"
    private String password;

    @Column(name = "friendsCount", nullable = false)  // Налаштовує відображення поля в стовпець кількість друзів
    private int friendsCount;

    @Column(name = "followingCount", nullable = false)  // Налаштовує відображення поля в стовпець кількість підписок
    private int followingCount;

    @Column(name = "followersCount", nullable = false)  // Налаштовує відображення поля в стовпець кількість підписників
    private int followersCount;

     // Конструктор за замовчуванням
     public Account() {}

     // Конструктор з параметрами
     public Account(String username, String password) {
         this.username = username;
         this.password = password;
         this.friendsCount = 0;
         this.followingCount = 0;
         this.followersCount = 0;
     }

    // Геттери та сеттери для доступу до полів класу
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }
}
