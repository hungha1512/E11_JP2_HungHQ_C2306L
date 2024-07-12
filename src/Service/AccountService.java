package Service;

import Entity.Account;
import Entity.Currency;
import Entity.Customer;
import Global.FileGeneric;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountService implements FileGeneric<Account> {
    public static List<Account> accountList = new ArrayList<>();

    public static Account getAccount(String id) {
        return accountList.stream().filter(account -> account.getId().equals(id)).findFirst().get();
    }

    public static boolean checkExistAccount(String accountId) {
        return accountList.stream().anyMatch(c -> c.getId().equals(accountId));
    }

    @Override
    public List<Account> readFile(String filePath) {
        if (FileService.checkExistsFilePath(filePath)) {
            try {
                FileReader fr = new FileReader(filePath);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        Account account = new Account();
                        Customer customer;
                        String[] splitLine = line.split(";\\s*");
                        account.setId(splitLine[0]);
                        customer = CustomerService.getCustomer(splitLine[1]);
                        account.setCustomer(customer);
                        account.setBalance(Double.parseDouble(splitLine[2]));
                        account.setCurrency(Currency.valueOf(splitLine[3]));
                        accountList.add(account);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("File does not exist");
        }
        return accountList;
    }

    @Override
    public Account writeFile(String filePath, Account account) {
        if (FileService.checkExistsFilePath(filePath)) {
            try {
                List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(filePath)));
                String accountId = String.valueOf(account.getId());
                boolean found = false;
                for (int i = 0; i < fileContent.size(); i++) {
                    String[] parts = fileContent.get(i).split(";");
                    if (parts.length > 0 && parts[0].trim().equals(accountId)) {
                        fileContent.set(i,
                                account.getId() + "; " +
                                account.getCustomer().getId() + "; " +
                                account.getBalance() + "; " +
                                account.getCurrency());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Account not found in file");
                } else {
                    Files.write(Paths.get(filePath), fileContent);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return account;
        } else {
            System.out.println("File does not exist");
        }
        return null;
    }
}
