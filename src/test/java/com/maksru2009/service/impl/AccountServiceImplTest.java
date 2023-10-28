package com.maksru2009.service.impl;

import static org.assertj.core.api.Assertions.*;

import com.maksru2009.dao.impl.AccountDaoImpl;
import com.maksru2009.entity.Account;
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

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountServiceImplTest {
    @Mock
    private AccountDaoImpl accountDao;
    @InjectMocks
    private AccountServiceImpl accountService;
    private List<Account> accounts;
    @BeforeAll
    void setUp(){
        accountDao = Mockito.mock(AccountDaoImpl.class);
        accounts = List.of(Account.builder().id(555).amount(634).build(), Account.builder().id(200).amount(123).build());
    }
    @Test
    void getById() throws SQLException {
        Account account = accounts.get(1);
        Mockito.when(accountDao.getById(1)).thenReturn(account);

        assertThat(accountService.getById(1)).isEqualTo(account);
    }

    @Test
    void addAccount() throws SQLException {
        accountService.addAccount(accounts.get(0));

        Mockito.verify(accountDao).addAccount(accounts.get(0));
    }

    @Test
    void getAllAccounts() throws SQLException {
        Mockito.when(accountDao.getAllAccounts()).thenReturn(accounts);

        assertThat(accountService.getAllAccounts()).isEqualTo(accounts);
    }

    @Test
    void getAccountsFromUser() throws SQLException {
        Mockito.when(accountDao.getAccountsFromUser(1)).thenReturn(accounts);

        assertThat(accountService.getAccountsFromUser(1)).isEqualTo(accounts);
    }

    @Test
    void getAccountsFromBank() throws SQLException {
        Mockito.when(accountDao.getAccountsFromBank(1)).thenReturn(accounts);

        assertThat(accountService.getAccountsFromBank(1)).isEqualTo(accounts);
    }

    @Test
    void getAccountsByUserIdAndBankId() throws SQLException {
        Mockito.when(accountDao.getAccountsByUserIdAndBankId(1,1)).thenReturn(accounts);

        assertThat(accountService.getAccountsByUserIdAndBankId(1,1)).isEqualTo(accounts);
    }

    @Test
    void updateAccount() throws SQLException {
        accountService.updateAccount(accounts.get(1));

        Mockito.verify(accountDao).updateAccount(accounts.get(1));
    }

    @Test
    void deleteAccountById() throws SQLException {
        accountService.deleteAccountById(1);

        Mockito.verify(accountDao).deleteAccountById(1);
    }

    @Test
    void updateAmountAcc() throws SQLException {
        accountService.updateAmountAcc(accounts.get(1));

        Mockito.verify(accountDao).updateAmountAcc(accounts.get(1));
    }

    @Test
    void updateAmountTransaction() throws SQLException {
        accountService.updateAmountTransaction(accounts.get(1),accounts.get(0));

        Mockito.verify(accountDao).updateAmountTransaction(accounts.get(1),accounts.get(0));
    }
}