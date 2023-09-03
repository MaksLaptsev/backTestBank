package com.maksru2009.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    private int id;
    private String accountNumber;
    private double amount;
    private Timestamp dateOpen;
    private Bank bank;
    private User user;

}
