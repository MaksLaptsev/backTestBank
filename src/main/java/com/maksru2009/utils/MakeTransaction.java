package com.maksru2009.utils;

import com.maksru2009.dao.impl.AccountDaoImpl;
import com.maksru2009.entity.Account;
import com.maksru2009.entity.Transaction;
import com.maksru2009.type.TransactionType;

import java.sql.SQLException;


public class MakeTransaction {
    private AccountDaoImpl accountDao;

    public MakeTransaction(){
        accountDao = new AccountDaoImpl();
    }

    /**
     * Вспомогательный метод для проверки транзакций {@link Transaction},
     * определения допустимости операции(достаточно ли на балансе суммы, для совершения операции),
     * в случае удовлетвореня этого условия - сохранение обновленной информации об {@link Account} в БД
     * @param transaction - {@link Transaction}, которую нужно провести
     * @return - boolean
     * @throws SQLException - в случае неудачного обновления инф об {@link Account} в БД
     */
    public boolean checkAndFixTransaction(Transaction transaction) throws SQLException {
        Account send = accountDao.getById(transaction.getSendingAccount().getId());
        Account benef = accountDao.getById(transaction.getBeneficiaryAccount().getId());
        double amount = transaction.getAmount();
        if(transaction.getType() == TransactionType.OUTGOING){
            if(send.getAmount() > amount){
                send.setAmount(send.getAmount()-amount);
                benef.setAmount(benef.getAmount()+amount);
                accountDao.updateAmountTransaction(send,benef);
                return true;
            }else return false;
        } else if (transaction.getType() == TransactionType.ADDING) {
            send.setAmount(send.getAmount() + amount);
            accountDao.updateAmountAcc(send);
            return true;
        } else if (transaction.getType() == TransactionType.CASH) {
            if(send.getAmount() > amount){
                send.setAmount(send.getAmount() - amount);
                accountDao.updateAmountAcc(send);
            }else return false;
        }
        return true;
    }
}
