package com.maksru2009.dao.impl;

import com.maksru2009.DBconnection.GetConnection;
import com.maksru2009.dao.TransactionDao;
import com.maksru2009.entity.Account;
import com.maksru2009.entity.Bank;
import com.maksru2009.entity.Transaction;
import com.maksru2009.entity.User;
import com.maksru2009.type.TransactionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDaoImpl implements TransactionDao<Transaction> {
    private final Connection con;

    public TransactionDaoImpl() {
        con = GetConnection.getConnection();
    }

    /**
     * Метод для получения экземпляра объекта {@link Transaction} из БД
     * @param id - int id необходимого нам {@link Transaction}
     * @return - {@link Transaction}
     */
    @Override
    public Transaction getTransactionById(int id) throws SQLException {
        String sql = "select t.id as tran_id, t.type as tran_type, t.amount as tran_amount," +
                "t.timestamp as tran_timestamp, sb.id as sbank_id, sb.name as sbank_name," +
                "bb.id as bbank_id, bb.name as bbank_name, su.id as suser_id, su.name as suser_name," +
                "su.lastname as suser_lastname, su.secondname as suser_secondname, su.phonenumber as suser_phonenumber," +
                "bu.id as buser_id, bu.name as buser_name, bu.lastname as buser_lastname," +
                "bu.secondname as buser_secondname, bu.phonenumber as buser_phonenumber," +
                "sa.id as sacc_id, sa.accountnumber as sacc_accountnumber, sa.amount as sacc_amount," +
                "ba.id as bacc_id, ba.accountnumber as bacc_accountnumber, ba.amount as bacc_amount from banktest.transaction t " +
                "inner join banktest.bank sb on t.sendingbank_id = sb.id " +
                "inner join banktest.bank bb on t.beneficiarybank_id = bb.id " +
                "inner join banktest.user su on t.sendinguser_id = su.id" +
                " join banktest.user bu on t.beneficiaryuser_id = bu.id " +
                "inner join banktest.account sa on t.sendingaccount_id = sa.id " +
                "inner join banktest.account ba on t.beneficiaryaccount_id = ba.id " +
                "where t.id = ?";
        Transaction transaction = null;

        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setInt(1,id);
            try(ResultSet set = statement.executeQuery()){
                if (set.next()){
                    transaction = createTransactionFromRS(set);
                }
            }
        }
        return Optional.ofNullable(transaction).orElseGet(Transaction::new);
    }

    /**
     * Иетод для получения списка {@link Transaction} с определенного {@link Account}
     * @param accId - id {@link Account}, на основе которого необходисо получить списко {@link Transaction}
     * @return - список {@link Transaction}
     */
    @Override
    public List<Transaction> getTransactionFromAccount(int accId) throws SQLException {
        String sql = "select t.id as tran_id, t.type as tran_type, t.amount as tran_amount," +
                "t.timestamp as tran_timestamp, sb.id as sbank_id, sb.name as sbank_name," +
                "bb.id as bbank_id, bb.name as bbank_name, su.id as suser_id, su.name as suser_name," +
                "su.lastname as suser_lastname, su.secondname as suser_secondname, su.phonenumber as suser_phonenumber," +
                "bu.id as buser_id, bu.name as buser_name, bu.lastname as buser_lastname," +
                "bu.secondname as buser_secondname, bu.phonenumber as buser_phonenumber," +
                "sa.id as sacc_id, sa.accountnumber as sacc_accountnumber, sa.amount as sacc_amount," +
                "ba.id as bacc_id, ba.accountnumber as bacc_accountnumber, ba.amount as bacc_amount from banktest.transaction t " +
                "inner join banktest.bank sb on t.sendingbank_id = sb.id " +
                "inner join banktest.bank bb on t.beneficiarybank_id = bb.id " +
                "inner join banktest.user su on t.sendinguser_id = su.id" +
                " join banktest.user bu on t.beneficiaryuser_id = bu.id " +
                "inner join banktest.account sa on t.sendingaccount_id = sa.id " +
                "inner join banktest.account ba on t.beneficiaryaccount_id = ba.id " +
                "where t.account_id = ?";
        List<Transaction> list = new ArrayList<>();
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setInt(1,accId);
            try(ResultSet set = statement.executeQuery()){
                while (set.next()){
                    list.add(createTransactionFromRS(set));
                }
            }
        }
        return list;
    }

    /**
     * Метод для сохранения {@link Transaction} в БД и присвоения ему соответствующего {@link Account}
     * @param t - экземпляр {@link Transaction}
     * @param accountOwnerId - id объекта {@link Account} к которому будет относится {@link Transaction}
     * @return - inn id сохранненого объекта {@link Transaction}
     * @throws SQLException - в случае неудачного сохранения объекта
     */
    @Override
    public int saveTransaction(Transaction t, int accountOwnerId) throws SQLException {
        String sql = "insert into banktest.transaction (type, sendingbank_id, beneficiarybank_id, sendinguser_id," +
                " beneficiaryuser_id, sendingaccount_id, beneficiaryaccount_id, amount, timestamp, account_id) " +
                "values (?::mod,?,?,?,?,?,?,?,?,?)";
        int tId;
        try(PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1,t.getType().name());
            statement.setInt(2,t.getSendingBank().getId());
            statement.setInt(3,t.getBeneficiaryBank().getId());
            statement.setInt(4,t.getSendingUser().getId());
            statement.setInt(5,t.getBeneficiaryUser().getId());
            statement.setInt(6,t.getSendingAccount().getId());
            statement.setInt(7,t.getBeneficiaryAccount().getId());
            statement.setDouble(8,t.getAmount());
            statement.setTimestamp(9,t.getTimestamp());
            statement.setInt(10,accountOwnerId);
            int rows = statement.executeUpdate();
            if(rows == 0){
                throw new SQLException("Saving Transaction failed, no rows affected.");
            }
            try(ResultSet set = statement.getGeneratedKeys()){
                if(set.next()){
                    tId = set.getInt(1);
                }else throw new SQLException("Creating Transaction failed, no ID obtained.");
            }
        }
        return tId;
    }

    /**
     * Метод для удаления {@link Transaction} из БД
     * @param id - id необходмого для удаления {@link Transaction}
     * @throws SQLException - в случае неудачного удаления объекта(либо он не найден)
     */
    @Override
    public void deleteTransactionById(int id) throws SQLException {
        String sql = "delete from banktest.transaction where id = ?";
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setInt(1,id);
            int rows = statement.executeUpdate();
            if(rows == 0){
                throw new SQLException("Deleting Transaction failed, no rows affected. \n" +
                        "Transaction with ID = "+id+" not found");
            }
        }
    }

    /**
     * Метод для получения списка {@link Transaction}, соответствующих заданному промежутку времени
     * @param t1 - timestamp, начало заданного промежутка
     * @param t2 - timestamp, конец заданного промежутка
     * @param accID - id объекта {@link Account}, на основе которого нужны объекты {@link Transaction}
     * @return - список {@link Transaction}
     */
    @Override
    public List<Transaction> getTransactionBetweenDate(Timestamp t1, Timestamp t2, int accID) throws SQLException {
        String sql = """
        select t.id as tran_id, t.type as tran_type, t.amount as tran_amount, 
        t.timestamp as tran_timestamp, sb.id as sbank_id, sb.name as sbank_name, 
                bb.id as bbank_id, bb.name as bbank_name, su.id as suser_id, su.name as suser_name,
                su.lastname as suser_lastname, su.secondname as suser_secondname, su.phonenumber as suser_phonenumber,
                bu.id as buser_id, bu.name as buser_name, bu.lastname as buser_lastname,
                bu.secondname as buser_secondname, bu.phonenumber as buser_phonenumber,
                sa.id as sacc_id, sa.accountnumber as sacc_accountnumber, sa.amount as sacc_amount,
                ba.id as bacc_id, ba.accountnumber as bacc_accountnumber, ba.amount as bacc_amount 
                from (select * from banktest.transaction where timestamp between ? and ?) as t
                inner join banktest.bank sb on t.sendingbank_id = sb.id\s
                inner join banktest.bank bb on t.beneficiarybank_id = bb.id\s
                inner join banktest.user su on t.sendinguser_id = su.id
                inner join banktest.user bu on t.beneficiaryuser_id = bu.id\s
                inner join banktest.account sa on t.sendingaccount_id = sa.id\s
                inner join banktest.account ba on t.beneficiaryaccount_id = ba.id\s
                where t.account_id = ?
        """;

        List<Transaction> list = new ArrayList<>();
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setTimestamp(1,t1);
            statement.setTimestamp(2,t2);
            statement.setInt(3,accID);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                list.add(createTransactionFromRS(set));
            }
        }
        return list;
    }

    @Override
    public void updateTransaction(Transaction object) throws SQLException {

    }

    /**
     * Вспомогательный метод для создания экземпляра {@link Transaction} на основе ResulSet полученного из БД
     * @param set - ResulSet, полученный от БД
     * @return - экземпляр {@link Transaction}, соответствующий полученному ResulSet'у
     */
    private Transaction createTransactionFromRS(ResultSet set) throws SQLException {
        Transaction transaction = Transaction.builder()
                .id(set.getInt("tran_id"))
                .amount(set.getDouble("tran_amount"))
                .type(TransactionType.valueOf(set.getString("tran_type")))
                .timestamp(set.getTimestamp("tran_timestamp"))
                .beneficiaryBank(Bank.builder()
                        .id(set.getInt("bbank_id"))
                        .name(set.getString("bbank_name"))
                        .build())
                .beneficiaryUser(User.builder()
                        .id(set.getInt("buser_id"))
                        .name(set.getString("buser_name"))
                        .lastName(set.getString("buser_lastname"))
                        .secondName(set.getString("buser_secondname"))
                        .phoneNumber(set.getString("buser_phonenumber"))
                        .build())
                .beneficiaryAccount(Account.builder()
                        .id(set.getInt("bacc_id"))
                        .accountNumber(set.getString("bacc_accountnumber"))
                        .amount(set.getDouble("bacc_amount"))
                        .build())
                .sendingBank(Bank.builder()
                        .id(set.getInt("sbank_id"))
                        .name(set.getString("sbank_name"))
                        .build())
                .sendingUser(User.builder()
                        .id(set.getInt("suser_id"))
                        .name(set.getString("suser_name"))
                        .lastName(set.getString("suser_lastname"))
                        .secondName(set.getString("suser_secondname"))
                        .phoneNumber(set.getString("suser_phonenumber"))
                        .build())
                .sendingAccount(Account.builder()
                        .id(set.getInt("sacc_id"))
                        .accountNumber(set.getString("sacc_accountnumber"))
                        .amount(set.getDouble("sacc_amount"))
                        .build())
                .build();
        transaction.getSendingAccount().setBank(transaction.getSendingBank());
        transaction.getSendingAccount().setUser(transaction.getSendingUser());
        transaction.getBeneficiaryAccount().setBank(transaction.getBeneficiaryBank());
        transaction.getBeneficiaryAccount().setUser(transaction.getBeneficiaryUser());
        return transaction;
    }
}
