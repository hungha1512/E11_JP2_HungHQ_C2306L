import Entity.*;
import Service.AccountService;
import Service.CustomerService;
import Service.TransactionService;
import Thread.DepositThread;
import Thread.WithdrawalThread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String rootPath = System.getProperty("user.dir");
        String accountPath = rootPath.replace("\\", "/") + "/data/Account.txt";
        String customerPath = rootPath.replace("\\", "/") + "/data/Customer.txt";
        String transactionPath = rootPath.replace("\\", "/") + "/data/Transaction.txt";

        List<Account> accounts = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();

        AccountService accountService = new AccountService();
        CustomerService customerService = new CustomerService();
        TransactionService transactionService = new TransactionService() {
            @Override
            public void transactionRequest() {

            }
        };
        customers = customerService.readFile(customerPath);
        CustomerService.customerList = customers;

        accounts = accountService.readFile(accountPath);
        AccountService.accountList = accounts;

        transactions = transactionService.readFile(transactionPath);
        TransactionService.setTransactionList(transactions);

        Transaction transaction = new Transaction();
        Account account = new Account();

        DepositThread depositThread;
        WithdrawalThread withdrawalThread;

        Thread withdrawalT, depositT;
        LocalDateTime startDate, endDate;


        String choice, flag = "", accountId, action, saveFile = null;
        double amount = 0;
        boolean isActive;

        try {
            do {
                System.out.print("Please enter the account ID: ");
                accountId = br.readLine();
                if (!AccountService.checkExistAccount(accountId)) {
                    System.out.println("Account does not exist");
                } else {
                    System.out.println("-------MENU-------");
                    System.out.println("1. Transaction");
                    System.out.println("2. Query balance by Account ID");
                    System.out.println("3. Query balance by time");
                    System.out.println("4. Don't know");
                    System.out.print("Your choice: ");
                    choice = br.readLine();
                    switch (choice) {
                        case "1":
//                      Create new Transaction
                            transaction = new Transaction();
//                      Add Transaction ID, time
                            transaction.setId(String.valueOf(transactions.size() + 1));
                            transaction.setLocalDateTime(LocalDateTime.now());
                            transaction.setAccount(AccountService.getAccount(accountId));
                            System.out.print("Enter amount: ");
                            amount = Double.parseDouble(br.readLine());
                            transaction.setAmount(amount);
                            System.out.println("What do you want to do? (W for WITHDRAWAL / D for DEPOSIT)");
                            action = br.readLine();
                            if (!action.equalsIgnoreCase("W") && !action.equalsIgnoreCase("D")) {
                                System.out.println("Invalid action");
                            } else {
                                if (action.equalsIgnoreCase("W") && TransactionService.checkRequirementTransaction(accountId, amount)) {
                                    transaction.setType(Type.WITHDRAWAL);
                                    transaction.setStatus(Status.C);
                                    transactions.add(transaction);
//                              Set Thread
                                    withdrawalThread = new WithdrawalThread(transaction);
                                    withdrawalT = new Thread(withdrawalThread);
                                    withdrawalT.start();
                                    withdrawalT.join();
                                } else if (action.equalsIgnoreCase("W")) {
                                    System.out.println("Transaction requirements are not met");
                                    transaction.setType(Type.WITHDRAWAL);
                                    transaction.setStatus(Status.R);
                                    transactions.add(transaction);
                                } else if (action.equalsIgnoreCase("D")) {
                                    transaction.setType(Type.DEPOSIT);
                                    transaction.setStatus(Status.C);
                                    transactions.add(transaction);
//                              Set Thread
                                    depositThread = new DepositThread(transaction);
                                    depositT = new Thread(depositThread);
                                    depositT.start();
                                    depositT.join();
                                }
                            }
//                      Update file
                            transactionService.writeFile(transactionPath, transaction);
                            accountService.writeFile(accountPath, transaction.getAccount());
                            break;
                        case "2":
                            System.out.println("Your balance is: " + AccountService.getAccount(accountId).getBalance());
                            break;
                        case "3":
                            System.out.print("Enter the start date (dd/MM/yyyy): ");
                            startDate = LocalDateTime.of(LocalDate.parse(br.readLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalTime.of(7, 0));
                            System.out.print("Enter the end date (dd/MM/yyyy): ");
                            endDate = LocalDateTime.of(LocalDate.parse(br.readLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalTime.of(7, 0));
                            if (startDate.isAfter(endDate)) {
                                System.out.println("Start day cannot after end day.");
                            } else {
                                TransactionService.printFilteredTransactions(accountId, startDate, endDate);
                                System.out.println("Do you want to save search history? (Y/N)");
                                saveFile = br.readLine();
                            }
                            if (saveFile.equalsIgnoreCase("y")) {
                                String fileName = rootPath.replace("\\", "/") + "/data/" + accountId + "_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".txt";
                                TransactionService.writeFileMultipleLines(fileName, transactions);
                                System.out.println("Transactions saved to " + fileName);
                            } else {
                                System.out.println("End of session.");
                            }
                            break;
                        case "4":
                            isActive = TransactionService.isCustomerActive(accountId);
                            if (!isActive){
                                System.out.println("You don't have any transactions over 30 days.");
                                account = TransactionService.addProfit(accountId);
                                System.out.println(account.objectToLine("; "));
                                accountService.writeFile(accountPath, account);
                            } else {
                                System.out.println("You don't meet the requirement. End of session.");
                            }
                            break;
                        default:
                            System.out.println("Invalid choice");
                    }
                    System.out.println("Do you want to continue? Y to continue, N to stop.");
                    System.out.print("Your choice: ");
                    flag = br.readLine();
                }

            } while (flag.equalsIgnoreCase("y"));
        } catch (
                Exception e) {
            System.out.println(e.getMessage());
        }
    }
}