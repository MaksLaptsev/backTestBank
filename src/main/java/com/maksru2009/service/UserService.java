package com.maksru2009.service;

import java.sql.SQLException;
import java.util.List;

public interface UserService <T>{
    T getById(int id) throws SQLException;
    List<T> getAll() throws SQLException;
    List<T> getAllUsersFromBank(int bankId) throws SQLException;
    int addUser(T object) throws SQLException;
    void addUserInBank(T object, int bankId) throws SQLException;
    void updateUser(T object) throws SQLException;
    void deleteUserById(int id) throws SQLException;
}
