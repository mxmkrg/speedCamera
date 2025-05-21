package model.wallet;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Transaction {
    @Builder.Default
    private final UUID transactionID = UUID.randomUUID();
    private final TransactionType transactionType;
    private final double amount;
    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();

    //Overloaded 1: No timestamp given -> creates a timestamp
    public static Transaction createTransaction(TransactionType transactionType, double amount) {
        return Transaction.builder()
                .transactionType(transactionType)
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .build();
    }

    //Overloaded 2: Timestamp given
    public static Transaction createTransaction(TransactionType transactionType, double amount, LocalDateTime timestamp) {
        return Transaction.builder()
                .transactionType(transactionType)
                .amount(amount)
                .timestamp(timestamp)
                .build();
    }
}
