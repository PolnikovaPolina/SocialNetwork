package com.test;

import com.socialnetwork.model.Account;
import com.socialnetwork.model.Publication;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;
import java.util.Arrays;
import java.util.List;

public class MenuTaskCreateAcount {

    private static final String BASE_URL = "http://localhost:9599/api/accounts";
    private RestTemplate restTemplate = new RestTemplate();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\nSelect an item from the menu:");
            System.out.println("1 - Register a new account.");
            System.out.println("2 - Login to an account.");
            System.out.println("3 - Access protected resource.");
            System.out.println("4 - Display all accounts.");
            System.out.println("5 - Add a publication.");
            System.out.println("6 - Display publications for an account.");
            System.out.println("7 - Exit.");
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
                    displayAllAccounts();
                    break;
                case 5:
                    addPublication();
                    break;
                case 6:
                    displayPublicationsForAccount();
                    break;
                case 7:
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

    private void displayAllAccounts() {
        // Викликає метод restTemplate для відправлення GET-запиту до віддаленого сервера і отримання масиву об'єктів Account
        ResponseEntity<Account[]> response = restTemplate.getForEntity(BASE_URL, Account[].class);
        List<Account> accounts = Arrays.asList(response.getBody());

        accounts.forEach(account -> System.out.println(
            "ID: " + account.getId() +
            ", Username: " + account.getUsername() +
            ", Friends: " + account.getFriendsCount() +
            ", Following: " + account.getFollowingCount() +
            ", Followers: " + account.getFollowersCount()
        ));
    }

    private void addPublication() {
        System.out.print("Enter account ID: ");
        Long accountId = Long.parseLong(scanner.nextLine());
        System.out.print("Enter publication title: ");
        String title = scanner.nextLine();
        System.out.print("Enter publication content: ");
        String content = scanner.nextLine();

        Publication publication = new Publication();
        publication.setTitle(title);
        publication.setContent(content);

        HttpEntity<Publication> request = new HttpEntity<>(publication);
        ResponseEntity<Publication> response = restTemplate.postForEntity(BASE_URL + "/" + accountId + "/publications", request, Publication.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Publication added successfully!");
        } else {
            System.out.println("Failed to add publication: " + response.getBody());
        }
    }

    private void displayPublicationsForAccount() {
        System.out.print("Enter account ID: ");
        Long accountId = Long.parseLong(scanner.nextLine());

        ResponseEntity<Publication[]> response = restTemplate.getForEntity(BASE_URL + "/" + accountId + "/publications", Publication[].class);
        List<Publication> publications = Arrays.asList(response.getBody());

        publications.forEach(publication -> System.out.println(
            "ID: " + publication.getId() +
            ", Title: " + publication.getTitle() +
            ", Content: " + publication.getContent()
        ));
    }


    public static void main(String[] args) 
    {
        MenuTaskCreateAcount menu = new MenuTaskCreateAcount();
        menu.start();
    }
}
