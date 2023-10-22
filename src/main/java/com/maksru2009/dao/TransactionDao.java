package com.maksru2009.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface TransactionDao <T>{
    T getTransactionById(int id) throws SQLException;
    List<T> getTransactionFromAccount(int accId) throws SQLException;
    int saveTransaction(T object,int id) throws SQLException;
    void deleteTransactionById(int id) throws SQLException;
    void updateTransaction(T object) throws SQLException;
    List<T> getTransactionBetweenDate(Timestamp t1,Timestamp t2, int accID) throws SQLException;

}
