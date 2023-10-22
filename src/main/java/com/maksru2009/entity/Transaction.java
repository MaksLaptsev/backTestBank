package com.maksru2009.entity;

import com.maksru2009.type.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    private int id;
    private TransactionType type;
    private double amount;
    private Timestamp timestamp;
    private Bank sendingBank;
    private Bank beneficiaryBank;
    private User sendingUser;
    private User beneficiaryUser;
    private Account sendingAccount;
    private Account beneficiaryAccount;

}
