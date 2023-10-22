package com.maksru2009.service.impl;

import com.maksru2009.dao.impl.AccountDaoImpl;
import com.maksru2009.entity.Account;
import com.maksru2009.service.AccountService;

import java.sql.SQLException;
import java.util.List;

public class AccountServiceImpl implements AccountService<Account> {
    private AccountDaoImpl accountDao;

    public AccountServiceImpl() {
        accountDao = new AccountDaoImpl();
    }

    /**
     * @see AccountDaoImpl#getById(int)
     */
    @Override
    public Account getById(int id) throws SQLException {
        return accountDao.getById(id);
    }

    /**
     * @see AccountDaoImpl#addAccount(Account)
     */
    @Override
    public int addAccount(Account object) throws SQLException {
        return accountDao.addAccount(object);
    }

    /**
     * @see AccountDaoImpl#getAllAccounts()
     */
    @Override
    public List<Account> getAllAccounts() throws SQLException {
        return accountDao.getAllAccounts();
    }

    /**
     * @see AccountDaoImpl#getAccountsFromUser(int)
     */
    @Override
    public List<Account> getAccountsFromUser(int userId) throws SQLException {
        return accountDao.getAccountsFromUser(userId);
    }

    /**
     * @see AccountDaoImpl#getAccountsFromBank(int)
     */
    @Override
    public List<Account> getAccountsFromBank(int bankId) throws SQLException {
        return accountDao.getAccountsFromBank(bankId);
    }

    /**
     * @see AccountDaoImpl#getAccountsByUserIdAndBankId(int, int)
     */
    @Override
    public List<Account> getAccountsByUserIdAndBankId(int userId, int bankId) throws SQLException {
        return accountDao.getAccountsByUserIdAndBankId(userId,bankId);
    }

    /**
     * @see AccountDaoImpl#updateAccount(Account)
     */
    @Override
    public void updateAccount(Account object) throws SQLException {
        accountDao.updateAccount(object);
    }

    /**
     * @see AccountDaoImpl#deleteAccountById(int)
     */
    @Override
    public void deleteAccountById(int id) throws SQLException {
        accountDao.deleteAccountById(id);
    }

    /**
     * @see AccountDaoImpl#updateAmountAcc(Account)
     */
    @Override
    public void updateAmountAcc(Account object) throws SQLException {
        accountDao.updateAmountAcc(object);
    }

    /**
     * @see AccountDaoImpl#updateAmountTransaction(Account, Account)
     */
    @Override
    public void updateAmountTransaction(Account sendObject, Account benefObject) throws SQLException {
        accountDao.updateAmountTransaction(sendObject, benefObject);
    }
}
