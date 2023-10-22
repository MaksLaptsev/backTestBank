package com.maksru2009.service;

import com.maksru2009.entity.Transaction;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface TransactionService <T>{
    T getTransactionById(int id) throws SQLException;
    List<T> getTransactionFromAccount(int accId) throws SQLException;
    int addTransaction(T object) throws SQLException;
    void deleteTransactionById(int id) throws SQLException;
    void updateTransaction(T object) throws SQLException;
    List<Transaction> getTransactionBetweenDate(Timestamp t1, Timestamp t2, int accID) throws SQLException;
}
