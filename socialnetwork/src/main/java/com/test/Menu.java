package com.test;

import com.socialnetwork.model.Account;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.Scanner;

public class Menu {

    private static final String BASE_URL = "http://localhost:9599/api/accounts";
    private RestTemplate restTemplate = new RestTemplate();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\nSelect an item from the menu:");
            System.out.println("1 - Register a new account.");
            System.out.println("2 - Login to an account.");
            System.out.println("3 - Access protected resource.");
            System.out.println("4 - Exit.");
            System.out.print("Enter your menu item: ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            
            switch (choice) {
                case 1:
                    registerAccount();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    accessProtectedResource();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void registerAccount() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Account account = new Account(username, password);

        HttpEntity<Account> request = new HttpEntity<>(account);
        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + "/register", request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed: " + response.getBody());
        }
    }

    private void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Account account = new Account(username, password);

        HttpEntity<Account> request = new HttpEntity<>(account);
        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + "/login", request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed: " + response.getBody());
        }
    }

    private void accessProtectedResource() {
        HttpHeaders headers = new HttpHeaders();
        // Add authentication headers if needed

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL + "/protected/me", HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Protected resource: " + response.getBody());
        } else {
            System.out.println("Access denied: " + response.getStatusCode());
        }
    }

    public static void main(String[] args) 
    {
        Menu menu = new Menu();
        menu.start();
    }
}
