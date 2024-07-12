package Service;

import Entity.Customer;
import Global.FileGeneric;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomerService implements FileGeneric<Customer> {
    public static List<Customer> customerList = new ArrayList<>();

    public static boolean checkExistCustomer(String customerId){
        return customerList.stream().anyMatch(c -> c.getId().equals(customerId));
    }

    public static Customer getCustomer(String customerId){
        return customerList.stream().filter(c -> c.getId().equals(customerId)).findFirst().get();
    }
    @Override
    public List<Customer> readFile(String filePath) {
        if (FileService.checkExistsFilePath(filePath)) {
            try {
                FileReader fr = new FileReader(filePath);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()){
                        Customer customer = new Customer();
                        String[] splitLine = line.split(";\\s*");
                        customer.setId(splitLine[0]);
                        customer.setName(splitLine[1]);
                        customer.setPhone(splitLine[2]);
                        customerList.add(customer);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("File does not exist");
        }
        return customerList;
    }

    @Override
    public Customer writeFile(String filePath, Customer customer) {
        if (FileService.checkExistsFilePath(filePath)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write("\n" +
                        customer.getId() + "; " +
                        customer.getName() + "; " +
                        customer.getPhone());
                writer.newLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return customer;
        } else {
            System.out.println("File does not exist");
        }
        return null;
    }
}
