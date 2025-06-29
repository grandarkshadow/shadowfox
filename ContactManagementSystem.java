import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ContactManagementSystem {
    private static final Logger LOGGER = Logger.getLogger(ContactManagementSystem.class.getName());
    private final ArrayList<Contact> contacts;
    private final Scanner scanner;

    public ContactManagementSystem() {
        this.contacts = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        ContactManagementSystem system = new ContactManagementSystem();
        system.start();
    }

    private void start() {
        while (true) {
            displayMenu();
            String choice = getUserInput("Enter your choice (1-5): ");
            if (choice.equals("5")) {
                LOGGER.info("Contact Management System shutting down.");
                System.out.println("Goodbye!");
                break;
            }
            processChoice(choice);
        }
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n=== Contact Management System ===");
        System.out.println("1. Add Contact");
        System.out.println("2. View Contacts");
        System.out.println("3. Update Contact");
        System.out.println("4. Delete Contact");
        System.out.println("5. Exit");
    }

    private String getUserInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private void processChoice(String choice) {
        try {
            switch (choice) {
                case "1": addContact(); break;
                case "2": viewContacts(); break;
                case "3": updateContact(); break;
                case "4": deleteContact(); break;
                default:
                    LOGGER.warning("Invalid choice: " + choice);
                    System.out.println("Invalid choice. Please select 1-5.");
            }
        } catch (ContactException e) {
            LOGGER.severe("Error: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addContact() throws ContactException {
        String name = getUserInput("Enter name: ");
        validateName(name);
        String email = getUserInput("Enter email: ");
        validateEmail(email);
        String phone = getUserInput("Enter phone: ");
        contacts.add(new Contact(name, email, phone));
        LOGGER.info("Contact added: " + name);
        System.out.println("Contact added successfully.");
    }

    private void viewContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts available.");
            return;
        }
        System.out.println("\n=== Contacts ===");
        for (int i = 0; i < contacts.size(); i++) {
            System.out.println((i + 1) + ". " + contacts.get(i));
        }
    }

    private void updateContact() throws ContactException {
        viewContacts();
        if (contacts.isEmpty()) return;
        int index = getContactIndex();
        String name = getUserInput("Enter new name: ");
        validateName(name);
        String email = getUserInput("Enter new email: ");
        validateEmail(email);
        String phone = getUserInput("Enter new phone: ");
        contacts.set(index, new Contact(name, email, phone));
        LOGGER.info("Contact updated at index: " + index);
        System.out.println("Contact updated successfully.");
    }

    private void deleteContact() throws ContactException {
        viewContacts();
        if (contacts.isEmpty()) return;
        int index = getContactIndex();
        Contact removed = contacts.remove(index);
        LOGGER.info("Contact deleted: " + removed.getName());
        System.out.println("Contact deleted successfully.");
    }

    private int getContactIndex() throws ContactException {
        String input = getUserInput("Enter contact number: ");
        try {
            int index = Integer.parseInt(input) - 1;
            if (index < 0 || index >= contacts.size()) {
                throw new ContactException("Invalid contact number.");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new ContactException("Invalid number format: " + input);
        }
    }

    private void validateName(String name) throws ContactException {
        if (name == null || name.trim().isEmpty()) {
            throw new ContactException("Name cannot be empty.");
        }
    }

    private void validateEmail(String email) throws ContactException {
        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new ContactException("Invalid email format.");
        }
    }
}

class Contact {
    private final String name;
    private final String email;
    private final String phone;

    public Contact(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Email: " + email + ", Phone: " + phone;
    }
}

class ContactException extends Exception {
    public ContactException(String message) {
        super(message);
    }
}
