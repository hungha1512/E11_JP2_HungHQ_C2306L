package Entity;

import Global.EntityGeneric;

import java.time.LocalDateTime;

public class Transaction implements EntityGeneric {
    private String id;
    private Account account;
    private double amount;
    private Type type;
    private LocalDateTime localDateTime;
    private Status status;

    public Transaction() {
        ;
    }

    public Transaction(String id, Account account, double amount, Type type, LocalDateTime localDateTime, Status status) {
        this.id = id;
        this.account = account;
        this.amount = amount;
        this.type = type;
        this.localDateTime = localDateTime;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String objectToLine(String separator) {
        StringBuilder builder = new StringBuilder();
        return builder
                .append(id)
                .append(separator)
                .append(account.getCustomer().getName())
                .append(separator)
                .append(amount)
                .append(separator)
                .append(type)
                .append(separator)
                .append(localDateTime)
                .append(separator)
                .append(status)
                .toString();
    }
}
