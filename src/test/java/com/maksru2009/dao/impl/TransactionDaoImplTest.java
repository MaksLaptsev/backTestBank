package com.maksru2009.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.maksru2009.builder.TransactionBuilder;
import com.maksru2009.entity.Transaction;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.*;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TransactionDaoImplTest {
    @InjectMocks
    TransactionDaoImpl transactionDao;
    @Mock
    private Connection con;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @Test
    @SneakyThrows
    void getTransactionById() {
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
        Transaction expected = TransactionBuilder.sTransaction().build();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doNothing().when(preparedStatement).setInt(1,expected.getId());
        Mockito.doReturn(resultSet).when(preparedStatement).executeQuery();
        Mockito.doReturn(true).when(resultSet).next();

        mockResulSetTransaction(expected);
        Transaction actual = transactionDao.getTransactionById(expected.getId());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    void getTransactionFromAccount() {
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
        Transaction transaction = TransactionBuilder.sTransaction().build();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doNothing().when(preparedStatement).setInt(Mockito.anyInt(),Mockito.anyInt());
        Mockito.doReturn(resultSet).when(preparedStatement).executeQuery();
        Mockito.doReturn(true,true,false).when(resultSet).next();

        mockResulSetTransaction(transaction);
        List<Transaction> expected = List.of(transaction,transaction);
        List<Transaction> actual = transactionDao.getTransactionFromAccount(1);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    void saveTransaction() {
        String sql = "insert into banktest.transaction (type, sendingbank_id, beneficiarybank_id, sendinguser_id," +
                " beneficiaryuser_id, sendingaccount_id, beneficiaryaccount_id, amount, timestamp, account_id) " +
                "values (?::mod,?,?,?,?,?,?,?,?,?)";
        Transaction transaction = TransactionBuilder.sTransaction().build();
        int idExpected = transaction.getId();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        Mockito.doNothing().when(preparedStatement).setInt(Mockito.anyInt(),Mockito.anyInt());
        Mockito.doNothing().when(preparedStatement).setString(Mockito.anyInt(),Mockito.any(String.class));
        Mockito.doNothing().when(preparedStatement).setDouble(Mockito.anyInt(),Mockito.anyDouble());
        Mockito.doNothing().when(preparedStatement).setTimestamp(Mockito.anyInt(),Mockito.any(Timestamp.class));
        Mockito.doReturn(2).when(preparedStatement).executeUpdate();
        Mockito.doReturn(resultSet).when(preparedStatement).getGeneratedKeys();
        Mockito.doReturn(true).when(resultSet).next();
        Mockito.doReturn(transaction.getId()).when(resultSet).getInt(1);

        int idActual = transactionDao.saveTransaction(transaction,1);

        assertThat(idActual).isEqualTo(idExpected);
    }

    @Test
    @SneakyThrows
    void deleteTransactionById() {
        String sql = "delete from banktest.transaction where id = ?";

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doNothing().when(preparedStatement).setInt(Mockito.anyInt(),Mockito.anyInt());
        Mockito.doReturn(2).when(preparedStatement).executeUpdate();

        transactionDao.deleteTransactionById(1);

        Mockito.verify(preparedStatement).executeUpdate();
    }

    @Test
    @SneakyThrows
    void getTransactionBetweenDate() {
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
        Transaction transaction = TransactionBuilder.sTransaction().build();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doNothing().when(preparedStatement).setTimestamp(Mockito.anyInt(),Mockito.any(Timestamp.class));
        Mockito.doNothing().when(preparedStatement).setInt(Mockito.anyInt(),Mockito.anyInt());
        Mockito.doReturn(resultSet).when(preparedStatement).executeQuery();
        Mockito.doReturn(true,false).when(resultSet).next();

        List<Transaction> expected = List.of(transaction);
        mockResulSetTransaction(transaction);
        List<Transaction> actual = transactionDao.getTransactionBetweenDate(new Timestamp(123456),new Timestamp(234567),1);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    void updateTransaction() {
        transactionDao.updateTransaction(TransactionBuilder.sTransaction().build());
    }

    private void mockResulSetTransaction(Transaction t) throws SQLException {
        Mockito.doReturn(t.getId()).when(resultSet).getInt("tran_id");
        Mockito.doReturn(t.getAmount()).when(resultSet).getDouble("tran_amount");
        Mockito.doReturn(t.getType().toString()).when(resultSet).getString("tran_type");
        Mockito.doReturn(t.getTimestamp()).when(resultSet).getTimestamp("tran_timestamp");

        Mockito.doReturn(t.getBeneficiaryBank().getId()).when(resultSet).getInt("bbank_id");
        Mockito.doReturn(t.getBeneficiaryBank().getName()).when(resultSet).getString("bbank_name");

        Mockito.doReturn(t.getBeneficiaryUser().getId()).when(resultSet).getInt("buser_id");
        Mockito.doReturn(t.getBeneficiaryUser().getName()).when(resultSet).getString("buser_name");
        Mockito.doReturn(t.getBeneficiaryUser().getLastName()).when(resultSet).getString("buser_lastname");
        Mockito.doReturn(t.getBeneficiaryUser().getSecondName()).when(resultSet).getString("buser_secondname");
        Mockito.doReturn(t.getBeneficiaryUser().getPhoneNumber()).when(resultSet).getString("buser_phonenumber");

        Mockito.doReturn(t.getBeneficiaryAccount().getId()).when(resultSet).getInt("bacc_id");
        Mockito.doReturn(t.getBeneficiaryAccount().getAccountNumber()).when(resultSet).getString("bacc_accountnumber");
        Mockito.doReturn(t.getBeneficiaryAccount().getAmount()).when(resultSet).getDouble("bacc_amount");

        Mockito.doReturn(t.getSendingBank().getId()).when(resultSet).getInt("sbank_id");
        Mockito.doReturn(t.getSendingBank().getName()).when(resultSet).getString("sbank_name");

        Mockito.doReturn(t.getSendingUser().getId()).when(resultSet).getInt("suser_id");
        Mockito.doReturn(t.getSendingUser().getName()).when(resultSet).getString("suser_name");
        Mockito.doReturn(t.getSendingUser().getLastName()).when(resultSet).getString("suser_lastname");
        Mockito.doReturn(t.getSendingUser().getSecondName()).when(resultSet).getString("suser_secondname");
        Mockito.doReturn(t.getSendingUser().getPhoneNumber()).when(resultSet).getString("suser_phonenumber");

        Mockito.doReturn(t.getSendingAccount().getId()).when(resultSet).getInt("sacc_id");
        Mockito.doReturn(t.getSendingAccount().getAccountNumber()).when(resultSet).getString("sacc_accountnumber");
        Mockito.doReturn(t.getSendingAccount().getAmount()).when(resultSet).getDouble("sacc_amount");
    }
}