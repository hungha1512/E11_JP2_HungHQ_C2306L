package Entity;

import Global.EntityGeneric;
import org.w3c.dom.ls.LSOutput;

public class Account implements EntityGeneric {
    private String id;
    private Customer customer;
    private double balance;
    private Currency currency;

    public Account() {
        ;
    }

    public Account(String id, Customer customer, double balance, Currency currency) {
        this.id = id;
        this.customer = customer;
        this.balance = balance;
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String objectToLine(String separator) {
        StringBuilder builder = new StringBuilder();
        return builder
                .append(id)
                .append(separator)
                .append(customer.getName())
                .append(separator)
                .append(balance)
                .append(separator)
                .append(currency)
                .toString();
    }
}
