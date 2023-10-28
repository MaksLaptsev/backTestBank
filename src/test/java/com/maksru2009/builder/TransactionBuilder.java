package com.maksru2009.builder;

import com.maksru2009.entity.Transaction;
import com.maksru2009.type.TransactionType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor(staticName = "sTransaction")
public class TransactionBuilder implements TestBuilder<Transaction>{
    private int id = 1;
    private TransactionType type = TransactionType.CASH;
    private double amount = 150.0;
    private Timestamp timestamp = new Timestamp(555555);

    @Override
    public Transaction build() {
        return Transaction.builder()
                .id(id)
                .type(type)
                .amount(amount)
                .timestamp(timestamp)
                .beneficiaryAccount(AccountBuilder.sAccount().withDateOpen(null).build())
                .sendingAccount(AccountBuilder.sAccount()
                        .withDateOpen(null)
                        .withAccountNumber("WWWWWWWWWWWWWWWW")
                        .withId(2)
                        .withAmount(777)
                        .build())
                .sendingUser(UserBuilder.sUser().build())
                .beneficiaryUser(UserBuilder.sUser().build())
                .beneficiaryBank(BankBuilder.sBank().build())
                .sendingBank(BankBuilder.sBank().build())
                .build();
    }
}
