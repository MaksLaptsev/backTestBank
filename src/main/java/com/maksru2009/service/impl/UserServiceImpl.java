package com.maksru2009.service.impl;

import com.maksru2009.dao.impl.UserDaoImpl;
import com.maksru2009.entity.User;
import com.maksru2009.service.UserService;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService<User> {
    private UserDaoImpl userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    /**
     * @see UserDaoImpl#getById(int)
     */
    @Override
    public User getById(int id) throws SQLException {
        return userDao.getById(id);
    }

    /**
     * @see UserDaoImpl#getAll()
     */
    @Override
    public List<User> getAll() throws SQLException {
        return userDao.getAll();
    }

    /**
     * @see UserDaoImpl#getAllUsersFromBank(int) )
     */
    @Override
    public List<User> getAllUsersFromBank(int bankId) throws SQLException {
        return userDao.getAllUsersFromBank(bankId);
    }

    /**
     * @see UserDaoImpl#addUser(User)
     */
    @Override
    public int addUser(User object) throws SQLException {
        return userDao.addUser(object);
    }

    /**
     * @see UserDaoImpl#addUserInBank(User, int)
     */
    @Override
    public void addUserInBank(User object, int bankId) throws SQLException {
        userDao.addUserInBank(object,bankId);
    }

    /**
     * @see UserDaoImpl#updateUser(User)
     */
    @Override
    public void updateUser(User object) throws SQLException {
        userDao.updateUser(object);
    }

    /**
     * @see UserDaoImpl#deleteUserById(int)
     */
    @Override
    public void deleteUserById(int id) throws SQLException {
        userDao.deleteUserById(id);
    }
}
