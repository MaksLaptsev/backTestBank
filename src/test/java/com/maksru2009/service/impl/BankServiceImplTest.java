package com.maksru2009.service.impl;

import com.maksru2009.dao.impl.BankDaoImpl;
import com.maksru2009.entity.Bank;
import com.maksru2009.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BankServiceImplTest {
    @Mock
    private BankDaoImpl bankDao;
    @InjectMocks
    private BankServiceImpl bankService;
    private List<Bank> banks;
    private List<User> users;
    @BeforeAll
    void setUp(){
        bankDao = Mockito.mock(BankDaoImpl.class);
        users = List.of(User.builder().id(2).build(), User.builder().id(154).build());
        banks = List.of(Bank.builder().name("NoName").build(), Bank.builder().name("name").build());
    }
    @Test
    void getById() throws SQLException {
        Bank bank = banks.get(0);
        Mockito.when(bankDao.getById(1)).thenReturn(bank);

        assertThat(bankService.getById(1)).isEqualTo(bank);
    }

    @Test
    void addBank() throws SQLException {
        bankService.addBank(banks.get(0));

        Mockito.verify(bankDao).addBank(banks.get(0));
    }

    @Test
    void getAllBanks() throws SQLException {
        Mockito.when(bankDao.getAllBanks()).thenReturn(banks);

        assertThat(bankService.getAllBanks()).isEqualTo(banks);
    }

    @Test
    void updateBank() throws SQLException {
        bankService.updateBank(banks.get(0));

        Mockito.verify(bankDao).updateBank(banks.get(0));
    }

    @Test
    void deleteBankById() throws SQLException {
        bankService.deleteBankById(1);

        Mockito.verify(bankDao).deleteBankById(1);
    }

    @Test
    void getBanksByUserId() throws SQLException {
        Mockito.when(bankDao.getBanksByUserId(1)).thenReturn(banks);

        assertThat(bankService.getBanksByUserId(1)).isEqualTo(banks);
    }
}