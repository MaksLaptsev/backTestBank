package com.maksru2009.service.impl;

import com.maksru2009.dao.impl.TransactionDaoImpl;
import com.maksru2009.entity.Account;
import com.maksru2009.entity.Transaction;
import com.maksru2009.service.TransactionService;
import com.maksru2009.type.TransactionType;
import com.maksru2009.utils.MakeTransaction;
import com.maksru2009.utils.checkCreator.CreateFolder;
import com.maksru2009.utils.checkCreator.CreatePdf;
import com.maksru2009.utils.checkCreator.TypeOfCheck;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransactionServiceImpl implements TransactionService<Transaction> {
    private TransactionDaoImpl transactionDao;
    private AccountServiceImpl accountService;
    private MakeTransaction makeTransaction;

    public TransactionServiceImpl() {
        transactionDao = new TransactionDaoImpl();
        accountService = new AccountServiceImpl();
        makeTransaction = new MakeTransaction();
    }

    /**
     * @see TransactionDaoImpl#getTransactionById(int)
     */
    @Override
    public Transaction getTransactionById(int id) throws SQLException {
        return transactionDao.getTransactionById(id);
    }

    /**
     * @see TransactionDaoImpl#getTransactionFromAccount(int)
     */
    @Override
    public List<Transaction> getTransactionFromAccount(int accId) throws SQLException {
        return transactionDao.getTransactionFromAccount(accId);
    }

    /**
     * Метод для добавления {@link Transaction}, а так же проверке соответствия баланса желаемым операциям,
     * так же изменение поля type у {@link Transaction}, в зависимости от операции
     * @param object - экземпляр {@link Transaction}
     * @return - int id объекта {@link Transaction}, пренадлежавший инициатору этой транзакции
     * @throws SQLException - в случае неудачного сохранения, либо, если баланс отправителя/снятия денежных средств меньше,
     * чем сумма, которую хотят снять/отправить
     */
    @Override
    public int addTransaction(Transaction object) throws SQLException {
        int tranID;
        int ownerID;
        if (makeTransaction.checkAndFixTransaction(object)){
            object.setTimestamp(new Timestamp(new Date().getTime()));
            if(object.getSendingAccount().getId() == object.getBeneficiaryAccount().getId()){
                ownerID = transactionDao.saveTransaction(object, object.getBeneficiaryAccount().getId());
                tranID = ownerID;
                new CreatePdf().createRecipe(transactionDao.getTransactionById(tranID),
                        new CreateFolder().createFolder(TypeOfCheck.CHECK,object.getSendingUser().getId(),
                                object.getSendingBank().getName(),object.getSendingAccount().getAccountNumber())
                        ,TypeOfCheck.CHECK,object.getSendingAccount());

            }else {
                ownerID = transactionDao.saveTransaction(object,object.getSendingAccount().getId());
                tranID = ownerID;
                new CreatePdf().createRecipe(transactionDao.getTransactionById(tranID),
                        new CreateFolder().createFolder(TypeOfCheck.CHECK,object.getSendingUser().getId(),
                                object.getSendingBank().getName(),object.getSendingAccount().getAccountNumber())
                        ,TypeOfCheck.CHECK,object.getSendingAccount());

                object.setType(TransactionType.INCOMING);
                tranID = transactionDao.saveTransaction(object,object.getBeneficiaryAccount().getId());

                new CreatePdf().createRecipe(transactionDao.getTransactionById(tranID),
                        new CreateFolder().createFolder(TypeOfCheck.CHECK,object.getBeneficiaryUser().getId(),
                                object.getBeneficiaryBank().getName(),object.getBeneficiaryAccount().getAccountNumber())
                        ,TypeOfCheck.CHECK,object.getBeneficiaryAccount());
            }
        }else throw new SQLException("\n!!!! Insufficient funds to perform the operation !!!!\n");
        return ownerID;
    }

    /**
     * @see TransactionDaoImpl#deleteTransactionById(int)
     */
    @Override
    public void deleteTransactionById(int id) throws SQLException {
        transactionDao.deleteTransactionById(id);
    }

    /**
     * @see TransactionDaoImpl#updateTransaction(Transaction)
     */
    @Override
    public void updateTransaction(Transaction object) throws SQLException {
        transactionDao.updateTransaction(object);
    }

    /**
     * @see TransactionDaoImpl#getTransactionBetweenDate(Timestamp, Timestamp, int)
     * @throws - в случае, если никаких транзакций за заданный период не найдено
     */
    @Override
    public List<Transaction> getTransactionBetweenDate(Timestamp t1, Timestamp t2, int accID) throws SQLException {
        List<Transaction> list = transactionDao.getTransactionBetweenDate(t1, t2, accID);
        if(list.size() == 0){
            String mes = """
                    \n!!!! Transactions between %s - %s not found! !!!!
                    """.
                    formatted(new SimpleDateFormat("yyyy-MM-dddd").format(t1),
                            new SimpleDateFormat("yyyy-MM-dddd").format(t1));
            throw new SQLException(mes);
        }
        Account account = accountService.getById(accID);
        String path = new CreateFolder().createFolder(TypeOfCheck.EXTRACT,account.getUser().getId()
                ,account.getBank().getName(),account.getAccountNumber());
        try{
            new CreatePdf().createRecipe(list,path,TypeOfCheck.EXTRACT,account,new Timestamp(new Date().getTime()),t1,t2);

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return list;
    }
}
