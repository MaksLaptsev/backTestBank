package com.maksru2009.dao;

import java.sql.SQLException;
import java.util.List;

public interface AccountDao <T>{
    T getById(int id) throws SQLException;
    int addAccount(T object) throws SQLException;
    List<T> getAllAccounts() throws SQLException;
    List<T> getAccountsFromUser(int userId) throws SQLException;
    List<T> getAccountsFromBank(int bankId) throws SQLException;
    List<T> getAccountsByUserIdAndBankId(int userId, int bankId) throws SQLException;
    void updateAccount(T object) throws SQLException;
    void deleteAccountById(int id) throws SQLException;
    void updateAmountAcc(T object) throws SQLException;
    void updateAmountTransaction(T sendObject, T benefObject) throws SQLException;
}
