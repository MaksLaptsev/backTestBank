package com.maksru2009.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.maksru2009.builder.BankBuilder;
import com.maksru2009.entity.Bank;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class BankDaoImplTest {

    @InjectMocks
    private BankDaoImpl bankDao;
    @Mock
    private Connection con;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @Test
    @SneakyThrows
    void getById() {
        String sql = "select from banktest.bank where id = ?";
        Bank expected = BankBuilder.sBank().build();
        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doNothing().when(preparedStatement).setInt(1,expected.getId());
        Mockito.doReturn(resultSet).when(preparedStatement).executeQuery();
        Mockito.doReturn(true).when(resultSet).next();
        Mockito.doReturn(expected.getId()).when(resultSet).getInt("id");
        Mockito.doReturn(expected.getName()).when(resultSet).getString("name");

        Bank actual = bankDao.getById(expected.getId());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    void addBank() {
        String sql = "insert into banktest.bank (name) values (?)";
        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        Bank bank = BankBuilder.sBank().build();
        int idExpected = bank.getId();

        Mockito.doNothing().when(preparedStatement).setString(1,bank.getName());
        Mockito.doReturn(bank.getId()).when(preparedStatement).executeUpdate();
        Mockito.doReturn(resultSet).when(preparedStatement).getGeneratedKeys();
        Mockito.doReturn(true).when(resultSet).next();
        Mockito.doReturn(bank.getId()).when(resultSet).getInt(1);

        int idActual = bankDao.addBank(bank);

        assertThat(idActual).isEqualTo(idExpected);
    }

    @Test
    @SneakyThrows
    void getAllBanks() {
        String sql = "select * from banktest.bank";
        Bank bank = BankBuilder.sBank().build();
        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doReturn(resultSet).when(preparedStatement).executeQuery();
        Mockito.doReturn(true,true,false).when(resultSet).next();
        Mockito.doReturn(bank.getId()).when(resultSet).getInt("id");
        Mockito.doReturn(bank.getName()).when(resultSet).getString("name");

        List<Bank> expected = List.of(bank,bank);
        List<Bank> actual = bankDao.getAllBanks();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    void updateBank() {
        String sql = "update banktest.bank set name = ? where id = ?";
        Bank bank = BankBuilder.sBank().build();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doNothing().when(preparedStatement).setInt(2,bank.getId());
        Mockito.doNothing().when(preparedStatement).setString(1,bank.getName());
        Mockito.doReturn(2).when(preparedStatement).executeUpdate();

        bankDao.updateBank(bank);

        Mockito.verify(preparedStatement).executeUpdate();
    }

    @Test
    @SneakyThrows
    void deleteBankById() {
        String sql = "delete from banktest.bank where id = ?";
        Bank bank = BankBuilder.sBank().build();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doNothing().when(preparedStatement).setInt(1,bank.getId());
        Mockito.doReturn(2).when(preparedStatement).executeUpdate();

        bankDao.deleteBankById(bank.getId());

        Mockito.verify(preparedStatement).executeUpdate();
    }

    @Test
    @SneakyThrows
    void getBanksByUserId() {
        String sql ="select b.id as bank_id, b.name as bank_name from banktest.bank b " +
                "inner join banktest.bank_user bu on b.id = bu.bank_id " +
                "where bu.user_id = ?";
        Bank bank = BankBuilder.sBank().build();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doNothing().when(preparedStatement).setInt(Mockito.anyInt(),Mockito.anyInt());
        Mockito.doReturn(resultSet).when(preparedStatement).executeQuery();
        Mockito.doReturn(true,true,false).when(resultSet).next();
        Mockito.doReturn(bank.getId()).when(resultSet).getInt("bank_id");
        Mockito.doReturn(bank.getName()).when(resultSet).getString("bank_name");

        List<Bank> expected = List.of(bank,bank);
        List<Bank> actual = bankDao.getBanksByUserId(2);

        assertThat(actual).isEqualTo(expected);
    }
}
