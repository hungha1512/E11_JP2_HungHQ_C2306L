package Service;

import Entity.Account;
import Entity.Status;
import Entity.Transaction;
import Entity.Type;
import Global.FileGeneric;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Math.pow;


public abstract class TransactionService implements FileGeneric<Transaction> {
    private static List<Transaction> transactionList = new ArrayList<>();
    protected Transaction transaction;


    public TransactionService() {
        ;
    }

    public TransactionService(Transaction transaction) {
        this.transaction = transaction;
    }

    public static boolean checkRequirementTransaction(String accountId, double amount) {
        return amount % 10 == 0 && amount <= AccountService.getAccount(accountId).getBalance();
    }

    public static Transaction getTransaction(String id) {
        return transactionList.stream().filter(x -> x.getId().equals(id)).findFirst().get();
    }

    public static boolean checkExistTransaction(String transactionId) {
        return transactionList.stream().anyMatch(c -> c.getId().equals(transactionId));
    }

    public static void setTransactionList(List<Transaction> transactionList) {
        TransactionService.transactionList = transactionList;
    }

    public static Map<String, List<Transaction>> filterTransaction(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionList.stream()
                .filter(t -> t.getAccount().getId().equals(accountId))
                .filter(t -> t.getLocalDateTime().isAfter(startDate))
                .filter(t -> t.getLocalDateTime().isBefore(endDate))
                .collect(Collectors.groupingBy(t -> t.getAccount().getId()));
    }

    public static void printFilteredTransactions(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, List<Transaction>> filteredTransactions = filterTransaction(accountId, startDate, endDate);
        List<Transaction> transactions = filteredTransactions.get(accountId);
        if (transactions != null) {
            transactions.forEach(t -> System.out.println(t.objectToLine("; ")));
        } else {
            System.out.println("No transactions found for the given criteria.");
        }
    }

    public abstract void transactionRequest();

    public static void writeFileMultipleLines(String filePath, List<Transaction> transactions) {
        if (!FileService.checkExistsFilePath(filePath)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                for (Transaction transaction : transactions) {
                    writer.write(
                            transaction.getId() + "; " +
                                    transaction.getAccount().getId() + "; " +
                                    transaction.getAmount() + "; " +
                                    transaction.getType() + "; " +
                                    transaction.getLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss")) + "; " +
                                    transaction.getStatus().toString());
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("File has already existed");
        }
    }

    public static Account addProfit(String accountId) {
        Account account = AccountService.getAccount(accountId);
        Transaction lastTransaction = getLastTransaction(accountId);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastTransactionDateTime = lastTransaction.getLocalDateTime();
        long daysBetween = ChronoUnit.DAYS.between(lastTransactionDateTime, now);
        long period = daysBetween / 30;
        double profit = account.getBalance() * pow(1.02, period);
        account.setBalance(account.getBalance() + profit);
        System.out.println("Your profit is: " + profit);
        return account;
    }

    public static Transaction getLastTransaction(String accountId) {
        return transactionList.stream()
                .filter(transaction -> transaction.getAccount().getId().equals(accountId))
                .sorted((t1, t2) -> t2.getLocalDateTime().compareTo(t1.getLocalDateTime()))
                .findFirst().get();
    }

    public static List<String> checkAccount30Days() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thresholdDate = now.minusDays(30);

        Set<String> activeCustomers = transactionList.stream()
                .filter(transaction -> !transaction.getLocalDateTime().isAfter(thresholdDate))
                .map(transaction -> transaction.getAccount().getId())
                .collect(Collectors.toSet());

        Set<String> customers = transactionList.stream()
                .map(transaction -> transaction.getAccount().getId())
                .collect(Collectors.toSet());

        return customers.stream()
                .filter(customer -> !activeCustomers.contains(customer))
                .collect(Collectors.toList());
    }

    public static boolean isCustomerActive (String accountId) {
        List<String> inactiveCustomers = checkAccount30Days();
        return inactiveCustomers.contains(accountId);
    }

    @Override
    public List<Transaction> readFile(String filePath) {
        if (FileService.checkExistsFilePath(filePath)) {
            try {
                FileReader fr = new FileReader(filePath);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        Transaction transaction = new Transaction();
                        Account account;
                        String[] splitLine = line.split(";\\s*");
                        transaction.setId(splitLine[0]);
                        account = AccountService.getAccount(splitLine[1]);
                        transaction.setAccount(account);
                        transaction.setAmount(Double.parseDouble(splitLine[2]));
                        transaction.setType(Type.valueOf(splitLine[3]));
                        transaction.setLocalDateTime(LocalDateTime.parse(splitLine[4], DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss")));
                        transaction.setStatus(Status.valueOf(splitLine[5]));
                        transactionList.add(transaction);

                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("File does not exist");
        }
        return transactionList;
    }

    @Override
    public Transaction writeFile(String filePath, Transaction transaction) {
        if (FileService.checkExistsFilePath(filePath)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(
                        transaction.getId() + "; " +
                                transaction.getAccount().getId() + "; " +
                                transaction.getAmount() + "; " +
                                transaction.getType() + "; " +
                                transaction.getLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss")) + "; " +
                                transaction.getStatus().toString());
                writer.newLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return transaction;
        } else {
            System.out.println("File does not exist");
        }
        return null;
    }
}
