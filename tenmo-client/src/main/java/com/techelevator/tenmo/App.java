package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        Long id = currentUser.getUser().getId();
        System.out.println("Your current account balance is: $" + accountService.getBalance(id, currentUser));

    }

    private void viewTransferHistory() {

        Long userId = currentUser.getUser().getId();
        Transfer[] transfers = transferService.getTransferList(userId, currentUser);
        displayTransfers(transfers);

        Long userInput = consoleService.promptForLong("Please enter the Transfer ID to view details (0 to cancel): ");

        System.out.println(transferService.getTransferDetailsByTransferId(userId, userInput, currentUser));


    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub

    }

    private void sendBucks() {
        SecureUser[] secureUsers = accountService.getUserList();
        displayUsers(secureUsers);


       Long receiverId =  consoleService.promptForLong("Please enter ID of user you are sending to (0 to cancel): ");
       Long currentUserId = currentUser.getUser().getId();

        BigDecimal amountToSend = consoleService.promptForBigDecimal("Please enter amount: ");

        Transfer transfer = new Transfer();
        transfer.setAccountFrom(currentUserId);
        transfer.setAccountTo(receiverId);
        transfer.setAmount(amountToSend);


//        transferService.createTransfer(transfer, currentUserId, currentUser);
//        transferService.validateAndSend(amountToSend, receiverId, currentUserId, currentUser);

        transferService.sendBucks(transfer, currentUserId, currentUser);

    }


    private void displayUsers(SecureUser[] secureUsers) {
        if (secureUsers != null) {
            for (SecureUser secureUser : secureUsers) {
                System.out.println(secureUser.getId() + " " + secureUser.getUserName());
            }
        }
    }

    private void requestBucks() {
        // TODO Auto-generated method stub

    }

    private void displayTransfers(Transfer[] transfers) {
        if (transfers != null) {
            for (Transfer transfer : transfers) {
                System.out.println(transfer.getTransferId() + " " + transfer.getAccountFrom() + " " +  transfer.getAccountTo() + " " + transfer.getAmount());
            }

        }
    }
}