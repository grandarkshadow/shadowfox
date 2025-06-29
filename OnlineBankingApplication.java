// File: OnlineBankingApplication.java (Full Version with HTML Dashboard)
package com.example.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class OnlineBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineBankingApplication.class, args);
    }

    private final Map<Long, Account> accountStore = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();
    private static final String FILE_NAME = "accounts.txt";

    public OnlineBankingApplication() {
        loadAccountsFromFile();
    }

    // Create Account
    @PostMapping("/accounts")
    public Account createAccount(@RequestBody Account account) {
        long id = idGenerator.incrementAndGet();
        account.setId(id);
        accountStore.put(id, account);
        saveAccountsToFile();
        return account;
    }

    // Get Account by ID
    @GetMapping("/accounts/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountStore.get(id);
    }

    // List All Accounts
    @GetMapping("/accounts")
    public Collection<Account> listAccounts() {
        return accountStore.values();
    }

    // Transfer Funds
    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromId, @RequestParam Long toId, @RequestParam double amount) {
        Account from = accountStore.get(fromId);
        Account to = accountStore.get(toId);

        if (from == null || to == null) return "Account not found.";
        if (from.getBalance() < amount) return "Insufficient funds.";

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);
        saveAccountsToFile();
        return "Transfer successful.";
    }

    // Save accounts to file
    private void saveAccountsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Account acc : accountStore.values()) {
                writer.println(acc.getId() + "," + acc.getName() + "," + acc.getBalance());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load accounts from file
    private void loadAccountsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    Long id = Long.parseLong(parts[0]);
                    String name = parts[1];
                    double balance = Double.parseDouble(parts[2]);
                    Account acc = new Account(id, name, balance);
                    accountStore.put(id, acc);
                    idGenerator.set(Math.max(idGenerator.get(), id));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class: Account model
    static class Account {
        private Long id;
        private String name;
        private double balance;

        public Account() {}
        public Account(Long id, String name, double balance) {
            this.id = id;
            this.name = name;
            this.balance = balance;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getBalance() { return balance; }
        public void setBalance(double balance) { this.balance = balance; }
    }
}
