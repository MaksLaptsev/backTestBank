package com.maksru2009.service.impl;

import com.maksru2009.dao.impl.BankDaoImpl;
import com.maksru2009.entity.Bank;
import com.maksru2009.service.BankService;

import java.sql.SQLException;
import java.util.List;

public class BankServiceImpl implements BankService<Bank> {
    private BankDaoImpl bankDao;

    public BankServiceImpl() {
        bankDao = new BankDaoImpl();
    }

    /**
     * @see BankDaoImpl#getById(int)
     */
    @Override
    public Bank getById(int id) throws SQLException {
        return bankDao.getById(id);
    }

    /**
     * @see BankDaoImpl#addBank(Bank)
     */
    @Override
    public int addBank(Bank object) throws SQLException {
        return bankDao.addBank(object);
    }

    /**
     * @see BankDaoImpl#getAllBanks()
     */
    @Override
    public List<Bank> getAllBanks() throws SQLException {
        return bankDao.getAllBanks();
    }

    /**
     * @see BankDaoImpl#updateBank(Bank)
     */
    @Override
    public void updateBank(Bank object) throws SQLException {
        bankDao.updateBank(object);
    }

    /**
     * @see BankDaoImpl#deleteBankById(int)
     */
    @Override
    public void deleteBankById(int id) throws SQLException {
        bankDao.deleteBankById(id);
    }

    /**
     * @see BankDaoImpl#getBanksByUserId(int)
     */
    @Override
    public List<Bank> getBanksByUserId(int userId) throws SQLException {
        return bankDao.getBanksByUserId(userId);
    }
}
