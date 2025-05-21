package model.wallet;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Slf4j
public class Wallet {
    private double balance;
    private boolean active;

    //Composition 1:n: One wallet has multiple transactions
    private final List<Transaction> transactions = new ArrayList<>();

    public void deposit(double amount) {
        if (amount <= 0) {
            log.warn("Cannot deposit a non-positive amount: {}", amount);
            return;
        }

        balance += amount;
        transactions.add(Transaction.createTransaction(TransactionType.DEPOSIT, amount));
        log.info("Deposited {} into wallet. New balance: {}", amount, balance);
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            log.warn("Cannot withdraw a non-positive amount: {}", amount);
            return false;
        }

        if (balance < amount) {
            log.warn("Insufficient funds. Balance: {}, Attempted withdrawal: {}", balance, amount);
            return false;
        }

        balance -= amount;
        transactions.add(Transaction.createTransaction(TransactionType.WITHDRAWAL, amount));
        log.info("Withdrew {} from wallet. New balance {}", amount, balance);
        return true;
    }
}
