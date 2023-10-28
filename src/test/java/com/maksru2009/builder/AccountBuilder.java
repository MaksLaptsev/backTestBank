package com.maksru2009.builder;

import com.maksru2009.entity.Account;
import com.maksru2009.entity.Bank;
import com.maksru2009.entity.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor(staticName = "sAccount")
@With
public class AccountBuilder implements TestBuilder<Account>{
    private int id = 1;
    private String accountNumber = "AAAABBBBCCCCDDDD";
    private double amount = 322.0;
    private Timestamp dateOpen = new Timestamp(100000);
    private Bank bank = BankBuilder.sBank().build();
    private User user = UserBuilder.sUser().build();

    @Override
    public Account build() {
        return Account.builder()
                .id(id)
                .accountNumber(accountNumber)
                .amount(amount)
                .dateOpen(dateOpen)
                .bank(bank)
                .user(user)
                .build();
    }
}
