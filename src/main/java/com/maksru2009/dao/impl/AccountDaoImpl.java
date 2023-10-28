package com.maksru2009.dao.impl;

import com.maksru2009.DBconnection.GetConnection;
import com.maksru2009.dao.AccountDao;
import com.maksru2009.entity.Account;
import com.maksru2009.entity.Bank;
import com.maksru2009.entity.User;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AccountDaoImpl implements AccountDao<Account> {
    private Connection con;

    public AccountDaoImpl() {
        con = GetConnection.getConnection();
    }
    /**
     * Метод для получения объекта из БД на основе его id
     * @param id - int значение необходимого объекта из бд
     * @return - возвращает объект типа {@link Account}
     */
    @Override
    public Account getById(int id) throws SQLException {
        String sql = "select a.id as acc_id, a.accountnumber as acc_accountnumber," +
                " a.amount as acc_amount,a.dateopen as acc_dateopen, u.id as user_id, u.name as user_name," +
                "u.lastname as user_lastname, u.secondname as user_secondname, u.phonenumber as user_phonenumber," +
                "b.id as bank_id, b.name as bank_name from banktest.account a " +
                "inner join banktest.user u on a.user_id = u.id " +
                "inner join banktest.bank b on a.bank_id = b.id " +
                "where a.id = ?";
        Account account = null;
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setInt(1,id);
            try(ResultSet set = statement.executeQuery()){
                if(set.next()){
                    account = createAccFromRS(set);
                }
            }
        }
        return Optional.ofNullable(account).orElseGet(Account::new);
    }

    /**
     * Сохраняет новый экземпляр объекта {@link Account} в БД
     * @param acc - экземпляр {@link Account}
     * @return accID - int значение ID сохраненного объекта в БД
     * @throws SQLException - в случае неудачного сохранения объекта
     */
    @Override
    public int addAccount(Account acc) throws SQLException {
        String sql = "insert into banktest.account (accountnumber, amount, bank_id, user_id,dateopen) values (?,?,?,?,?)";
        int accID;
        try(PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1,acc.getAccountNumber());
            statement.setDouble(2,acc.getAmount());
            statement.setInt(3,acc.getBank().getId());
            statement.setInt(4,acc.getUser().getId());
            statement.setTimestamp(5,new Timestamp(new Date().getTime()));
            int rows = statement.executeUpdate();
            if(rows == 0){throw new SQLException("Creating Account failed, no rows affected.");}
            try(ResultSet set = statement.getGeneratedKeys()){
                if (set.next()){
                    accID = set.getInt(1);
                }else {throw new SQLException("Creating Account failed, no ID obtained.");}
            }
        }
        return accID;
    }

    /**
     * Метод для получения всех списка объектов {@link Account} из БД
     * @return возвращает список {@link Account}
     */
    @Override
    public List<Account> getAllAccounts() throws SQLException {
        String sql = "select * from banktest.account";
        List<Account> accountList = new ArrayList<>();
        try(PreparedStatement statement = con.prepareStatement(sql)){
            try(ResultSet set = statement.executeQuery()){
                while (set.next()){
                    accountList.add(Account.builder()
                            .id(set.getInt("id"))
                            .amount(set.getDouble("amount"))
                            .accountNumber(set.getString("accountnumber"))
                            .dateOpen(set.getTimestamp("dateopen"))
                            .build());
                }
            }
        }
        return accountList;
    }

    /**
     * Метод для получения списка с объектами {@link Account} определенного {@link User}
     * @param userId - id {@link User}, список {@link Account} которого нужно получить
     * @return возвращает список {@link Account}
     */
    @Override
    public List<Account> getAccountsFromUser(int userId) throws SQLException {
        String sql = "select a.id as acc_id, a.accountnumber as acc_accountnumber, a.amount as acc_amount," +
                "a.dateopen as acc_dateopen," +
                " u.id as user_id, u.name as user_name, u.lastname as user_lastname, u.secondname as user_secondname," +
                " u.phonenumber as user_phonenumber, b.id as bank_id, b.name as bank_name " +
                " from banktest.account a " +
                "inner join banktest.user u on a.user_id = u.id " +
                "inner join banktest.bank b on a.bank_id = b.id " +
                "where a.user_id = ?";
        List<Account> accountList = new ArrayList<>();
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setInt(1,userId);
            try(ResultSet set = statement.executeQuery()){
                while(set.next()){
                    accountList.add(createAccFromRS(set));
                }
            }
        }
        return accountList;
    }

    /**
     * Метод для получения списка с объектами {@link Account} определенного {@link Bank}
     * @param bankId - id {@link Bank}, список {@link Account} которого нужно получить
     * @return - возвращает список {@link Account}
     */
    @Override
    public List<Account> getAccountsFromBank(int bankId) throws SQLException {
        String sql = "select a.id as acc_id, a.accountnumber as acc_accountnumber, a.amount as acc_amount," +
                "a.dateopen as acc_dateopen," +
                " u.id as user_id, u.name as user_name, u.lastname as user_lastname, u.secondname as user_secondname," +
                " u.phonenumber as user_phonenumber, b.id as bank_id, b.name as bank_name " +
                " from banktest.account a " +
                "inner join banktest.user u on a.user_id = u.id " +
                "inner join banktest.bank b on a.bank_id = b.id " +
                "where a.bank_id = ?";
        List<Account> accountList = new ArrayList<>();
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setInt(1,bankId);
            try(ResultSet set = statement.executeQuery()){
                while (set.next()){
                    accountList.add(createAccFromRS(set));
                }
            }
        }
        return accountList;
    }

    /**
     * Метод для получения списка {@link Account} которые принадлежат определенному {@link Bank} и {@link User}
     * @param userId - id {@link Bank}, список {@link Account} которого нужно получить
     * @param bankId - id {@link User}, список {@link Account} которого нужно получить
     * @return - возвращает список {@link Account}
     */
    @Override
    public List<Account> getAccountsByUserIdAndBankId(int userId, int bankId) throws SQLException {
        String sql = "select a.id as acc_id, a.accountnumber as acc_accountnumber, a.amount as acc_amount," +
                "a.dateopen as acc_dateopen, " +
                " u.id as user_id, u.name as user_name, u.lastname as user_lastname, u.secondname as user_secondname," +
                " u.phonenumber as user_phonenumber, b.id as bank_id, b.name as bank_name " +
                " from banktest.account a " +
                "inner join banktest.user u on a.user_id = u.id " +
                "inner join banktest.bank b on a.bank_id = b.id " +
                "where a.bank_id = ? and a.user_id = ?";
        List<Account> accountList = new ArrayList<>();
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setInt(1,bankId);
            statement.setInt(2,userId);
            try(ResultSet set = statement.executeQuery()){
                while (set.next()){
                    accountList.add(createAccFromRS(set));
                }
            }
        }
        return accountList;
    }

    /**
     * Метод для обновления полей {@link Account} в БД
     * @param object - экземпляр {@link Account}, об котором нужно обновить инфориацию
     * @throws SQLException - в случае неудачного обновления объекта
     */
    @Override
    public void updateAccount(Account object) throws SQLException {
        String sql = "update banktest.account set accountnumber = ?,amount = ?,bank_id = ?,user_id =? where id = ?";
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setString(1,object.getAccountNumber());
            statement.setDouble(2,object.getAmount());
            statement.setInt(3,object.getBank().getId());
            statement.setInt(4,object.getUser().getId());
            statement.setInt(5,object.getId());
            int rows = statement.executeUpdate();
            if(rows == 0){
                throw new SQLException("Updating Account failed, no rows affected. \n" +
                        "Account with ID = "+object.getId()+" not found");
            }
        }
    }

    /**
     * Метод для обновления поля amount объекта {@link Account}
     * @param object - экземпляр {@link Account}, об котором нужно обновить инфориацию
     * @throws SQLException - в случае неудачного обновления объекта
     */
    @Override
    public void updateAmountAcc(Account object) throws SQLException {
        String sql = "update banktest.account set amount = ?" +
                "where id = ?";
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setDouble(1, object.getAmount());
            statement.setInt(2,object.getId());
            int rows = statement.executeUpdate();
            if(rows == 0){
                throw new SQLException("Updating Account failed, no rows affected. \n" +
                        "Account with ID = "+object.getId()+" not found");
            }
        }

    }

    /**
     * Метод для удаления информации из БД об объекте типа {@link Account}
     * @param id - id {@link Account}, который необходимо удалить
     * @throws SQLException - в случае неудачного удаления объекта/либо он не найден
     */
    @Override
    public void deleteAccountById(int id) throws SQLException {
        String sql = "delete from banktest.account where id = ?";
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setInt(1,id);
            int rows = statement.executeUpdate();
            if(rows == 0){
                throw new SQLException("Deleting Account failed, no rows affected. \n" +
                        "Account with ID = "+id+" not found");
            }
        }
    }

    /**
     * Метод для одноврменного обновления информации об балансе {@link Account} в рамках одной транзакции
     * @param sendObject - экземпляр {@link Account}, необходимый обновить
     * @param benefObject - экземпляр {@link Account}, необходимый обновить
     * @throws SQLException - в случае неудачного обновления объекта
     */
    @Override
    public void updateAmountTransaction(Account sendObject, Account benefObject) throws SQLException {
        String sql = "update banktest.account " +
                "set amount = nv.amount " +
                "from(values (?,?),(?,?)) " +
                "as nv (id,amount)" +
                "where banktest.account.id = nv.id";
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setInt(1,sendObject.getId());
            statement.setDouble(2,sendObject.getAmount());
            statement.setInt(3,benefObject.getId());
            statement.setDouble(4,benefObject.getAmount());
            int rows = statement.executeUpdate();
            if(rows == 0){
                throw new SQLException("Updating Accounts failed, no rows affected. \n" +
                        "Accounts with ID = "+sendObject.getId()+" and "+benefObject.getId()+" not found");
            }
        }
    }

    /**
     * Вспомогательный метод для сбора объекта {@link Account} из ResulSet полученного из БД
     * @param set - ResulSet полученный после запроса в БД
     * @return - {@link Account} соотвествующий этому ResulSet'у
     */
    private Account createAccFromRS(ResultSet set) throws SQLException {
        return Account.builder()
                .id(set.getInt("acc_id"))
                .accountNumber(set.getString("acc_accountnumber"))
                .amount(set.getDouble("acc_amount"))
                .dateOpen(set.getTimestamp("acc_dateopen"))
                .bank(Bank.builder()
                        .id(set.getInt("bank_id"))
                        .name(set.getString("bank_name"))
                        .build())
                .user(User.builder()
                        .id(set.getInt("user_id"))
                        .name(set.getString("user_name"))
                        .lastName(set.getString("user_lastname"))
                        .secondName(set.getString("user_secondname"))
                        .phoneNumber(set.getString("user_phonenumber"))
                        .build())
                .build();
    }
}
