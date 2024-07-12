package Thread;

import Entity.Status;
import Entity.Transaction;
import Service.TransactionService;

public class DepositThread extends TransactionService implements Runnable{
    public DepositThread(Transaction transaction) {
        super(transaction);
    }

    @Override
    public void transactionRequest() {
        transaction.getAccount().setBalance(transaction.getAccount().getBalance()+transaction.getAmount());
        System.out.println("Transaction Request Successfully");
        System.out.println("Your new balance is: " + transaction.getAccount().getBalance());

    }

    @Override
    public void run() {
        transactionRequest();
    }
}
