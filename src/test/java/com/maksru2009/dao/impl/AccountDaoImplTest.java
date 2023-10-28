package com.maksru2009.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.maksru2009.builder.AccountBuilder;
import com.maksru2009.entity.Account;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.sql.*;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccountDaoImplTest {

    @InjectMocks
    private AccountDaoImpl accountDao;
    @Mock
    private Connection con;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;


    @Test
    @SneakyThrows
    void getById() {
        String sql = "select a.id as acc_id, a.accountnumber as acc_accountnumber," +
                " a.amount as acc_amount,a.dateopen as acc_dateopen, u.id as user_id, u.name as user_name," +
                "u.lastname as user_lastname, u.secondname as user_secondname, u.phonenumber as user_phonenumber," +
                "b.id as bank_id, b.name as bank_name from banktest.account a " +
                "inner join banktest.user u on a.user_id = u.id " +
                "inner join banktest.bank b on a.bank_id = b.id " +
                "where a.id = ?";

        Account expected = AccountBuilder.sAccount().build();
        int id = expected.getId();
        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doNothing().when(preparedStatement).setInt(1,id);
        Mockito.doReturn(resultSet).when(preparedStatement).executeQuery();
        Mockito.doReturn(true).when(resultSet).next();
        getMockResulSet(expected);

        Account actual = accountDao.getById(id);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    void addAccount() {
        String sql = "insert into banktest.account (accountnumber, amount, bank_id, user_id,dateopen) values (?,?,?,?,?)";
        Account account = AccountBuilder.sAccount().build();
        int expected = account.getId();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        setMockStatement(account);
        int actual = accountDao.addAccount(account);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    void getAllAccounts() {
        String sql = "select * from banktest.account";
        Account account = AccountBuilder.sAccount().withBank(null).withUser(null).build();
        List<Account> expected = List.of(account,account);

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doReturn(resultSet).when(preparedStatement).executeQuery();
        Mockito.doReturn(true,true,false).when(resultSet).next();
        Mockito.doReturn(expected.get(0).getId()).when(resultSet).getInt("id");
        Mockito.doReturn(expected.get(0).getAccountNumber()).when(resultSet).getString("accountnumber");
        Mockito.doReturn(expected.get(0).getAmount()).when(resultSet).getDouble("amount");
        Mockito.doReturn(expected.get(0).getDateOpen()).when(resultSet).getTimestamp("dateopen");

        List<Account> actual = accountDao.getAllAccounts();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    void getAccountsFromUser() {
        String sql = "select a.id as acc_id, a.accountnumber as acc_accountnumber, a.amount as acc_amount," +
                "a.dateopen as acc_dateopen," +
                " u.id as user_id, u.name as user_name, u.lastname as user_lastname, u.secondname as user_secondname," +
                " u.phonenumber as user_phonenumber, b.id as bank_id, b.name as bank_name " +
                " from banktest.account a " +
                "inner join banktest.user u on a.user_id = u.id " +
                "inner join banktest.bank b on a.bank_id = b.id " +
                "where a.user_id = ?";
        Account expected = AccountBuilder.sAccount().build();
        int id = expected.getUser().getId();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doNothing().when(preparedStatement).setInt(1,id);
        Mockito.doReturn(resultSet).when(preparedStatement).executeQuery();
        Mockito.doReturn(true,false).when(resultSet).next();
        getMockResulSet(expected);

        List<Account> actual = accountDao.getAccountsFromUser(id);

        assertThat(actual).isEqualTo(List.of(expected));
    }

    @Test
    @SneakyThrows
    void getAccountsFromBank() {
        String sql = "select a.id as acc_id, a.accountnumber as acc_accountnumber, a.amount as acc_amount," +
                "a.dateopen as acc_dateopen," +
                " u.id as user_id, u.name as user_name, u.lastname as user_lastname, u.secondname as user_secondname," +
                " u.phonenumber as user_phonenumber, b.id as bank_id, b.name as bank_name " +
                " from banktest.account a " +
                "inner join banktest.user u on a.user_id = u.id " +
                "inner join banktest.bank b on a.bank_id = b.id " +
                "where a.bank_id = ?";

        Account expected = AccountBuilder.sAccount().build();
        int id = expected.getBank().getId();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doNothing().when(preparedStatement).setInt(1,id);
        Mockito.doReturn(resultSet).when(preparedStatement).executeQuery();
        Mockito.doReturn(true,false).when(resultSet).next();
        getMockResulSet(expected);

        List<Account> actual = accountDao.getAccountsFromBank(id);

        assertThat(actual).isEqualTo(List.of(expected));
    }

    @Test
    @SneakyThrows
    void getAccountsByUserIdAndBankId() {
        String sql = "select a.id as acc_id, a.accountnumber as acc_accountnumber, a.amount as acc_amount," +
                "a.dateopen as acc_dateopen, " +
                " u.id as user_id, u.name as user_name, u.lastname as user_lastname, u.secondname as user_secondname," +
                " u.phonenumber as user_phonenumber, b.id as bank_id, b.name as bank_name " +
                " from banktest.account a " +
                "inner join banktest.user u on a.user_id = u.id " +
                "inner join banktest.bank b on a.bank_id = b.id " +
                "where a.bank_id = ? and a.user_id = ?";

        Account expected = AccountBuilder.sAccount().build();
        int id = expected.getBank().getId();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doNothing().when(preparedStatement).setInt(1,id);
        Mockito.doReturn(resultSet).when(preparedStatement).executeQuery();
        Mockito.doReturn(true,false).when(resultSet).next();
        getMockResulSet(expected);

        List<Account> actual = accountDao.getAccountsByUserIdAndBankId(id,id);

        assertThat(actual).isEqualTo(List.of(expected));
    }

    @Test
    @SneakyThrows
    void updateAccount() {
        String sql = "update banktest.account set accountnumber = ?,amount = ?,bank_id = ?,user_id =? where id = ?";
        Account account = AccountBuilder.sAccount().build();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        setMockStatement(account);

        accountDao.updateAccount(account);
        Mockito.verify(preparedStatement,Mockito.times(1)).executeUpdate();
    }

    @Test
    @SneakyThrows
    void updateAmountAcc() {
        String sql = "update banktest.account set amount = ?" +
                "where id = ?";

        Account account = AccountBuilder.sAccount().build();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        setMockStatement(account);
        accountDao.updateAmountAcc(account);

        Mockito.verify(preparedStatement,Mockito.times(1)).executeUpdate();
    }

    @Test
    @SneakyThrows
    void deleteAccountById() {
        String sql = "delete from banktest.account where id = ?";

        int id = AccountBuilder.sAccount().build().getId();
        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doNothing().when(preparedStatement).setInt(1,id);
        Mockito.doReturn(2).when(preparedStatement).executeUpdate();

        accountDao.deleteAccountById(id);

        Mockito.verify(preparedStatement,Mockito.times(1)).executeUpdate();
    }

    @Test
    @SneakyThrows
    void updateAmountTransaction() {
        String sql = "update banktest.account " +
                "set amount = nv.amount " +
                "from(values (?,?),(?,?)) " +
                "as nv (id,amount)" +
                "where banktest.account.id = nv.id";

        Account account = AccountBuilder.sAccount().build();
        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        setMockStatement(account);

        accountDao.updateAmountTransaction(account,account);

        Mockito.verify(preparedStatement,Mockito.times(1)).executeUpdate();
    }


    private void setMockStatement(Account a) throws SQLException {
        Mockito.doNothing().when(preparedStatement).setString(1,a.getAccountNumber());
        Mockito.doNothing().when(preparedStatement).setDouble(2,a.getAmount());
        Mockito.doNothing().when(preparedStatement).setInt(3,a.getBank().getId());
        Mockito.doNothing().when(preparedStatement).setInt(4,a.getUser().getId());
        Mockito.doNothing().when(preparedStatement).setTimestamp(Mockito.anyInt(),Mockito.any());
        Mockito.doReturn(2).when(preparedStatement).executeUpdate();
        Mockito.doReturn(resultSet).when(preparedStatement).getGeneratedKeys();
        Mockito.doReturn(true).when(resultSet).next();
        Mockito.doReturn(a.getId()).when(resultSet).getInt(1);
    }

    private void getMockResulSet(Account a) throws SQLException {
        Mockito.doReturn(a.getId()).when(resultSet).getInt("acc_id");
        Mockito.doReturn(a.getAccountNumber()).when(resultSet).getString("acc_accountnumber");
        Mockito.doReturn(a.getAmount()).when(resultSet).getDouble("acc_amount");
        Mockito.doReturn(a.getDateOpen()).when(resultSet).getTimestamp("acc_dateopen");
        Mockito.doReturn(a.getUser().getId()).when(resultSet).getInt("user_id");
        Mockito.doReturn(a.getUser().getName()).when(resultSet).getString("user_name");
        Mockito.doReturn(a.getUser().getLastName()).when(resultSet).getString("user_lastname");
        Mockito.doReturn(a.getUser().getSecondName()).when(resultSet).getString("user_secondname");
        Mockito.doReturn(a.getUser().getPhoneNumber()).when(resultSet).getString("user_phonenumber");
        Mockito.doReturn(a.getBank().getId()).when(resultSet).getInt("bank_id");
        Mockito.doReturn(a.getBank().getName()).when(resultSet).getString("bank_name");
    }
}