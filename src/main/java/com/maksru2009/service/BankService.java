package com.maksru2009.service;

import java.sql.SQLException;
import java.util.List;

public interface BankService<T> {

    T getById(int id) throws SQLException;
    int addBank(T object) throws SQLException;
    List<T> getAllBanks() throws SQLException;
    void updateBank(T object) throws SQLException;
    void deleteBankById(int id) throws SQLException;
    List<T> getBanksByUserId(int userId) throws SQLException;
}
