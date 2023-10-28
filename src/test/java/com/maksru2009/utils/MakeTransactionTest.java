package com.maksru2009.utils;

import static org.assertj.core.api.Assertions.*;

import com.maksru2009.dao.impl.AccountDaoImpl;
import com.maksru2009.entity.Account;
import com.maksru2009.entity.Transaction;
import com.maksru2009.type.TransactionType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.SQLException;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MakeTransactionTest {
    @Mock
    private AccountDaoImpl accountDao;
    @InjectMocks
    private MakeTransaction makeTransaction;
    private Account accountSend;
    private Account accountBenef;

    private double amount;

    @BeforeAll
    void setUp() {
        accountDao = Mockito.mock(AccountDaoImpl.class);
        amount = 500d;
        accountSend = Account.builder()
                .id(1)
                .accountNumber("fdsgsdgsdg")
                .amount(5000)
                .build();
        accountBenef = Account.builder()
                .id(2)
                .accountNumber("asdasasd")
                .amount(3500)
                .build();
    }

    @Test
    void checkAndFixTransaction() throws SQLException {
        Mockito.when(accountDao.getById(1)).thenReturn(accountSend);
        Mockito.when(accountDao.getById(2)).thenReturn(accountBenef);

        assertThat(makeTransaction.checkAndFixTransaction(Transaction.builder()
                .amount(amount)
                .sendingAccount(accountSend)
                .beneficiaryAccount(accountBenef)
                .type(TransactionType.OUTGOING)
                .build())).isEqualTo(true);

        assertThat(makeTransaction.checkAndFixTransaction(Transaction.builder()
                .amount(amount)
                .sendingAccount(accountSend)
                .beneficiaryAccount(accountBenef)
                .type(TransactionType.CASH)
                .build())).isEqualTo(true);

        assertThat(makeTransaction.checkAndFixTransaction(Transaction.builder()
                .amount(amount)
                .sendingAccount(accountSend)
                .beneficiaryAccount(accountBenef)
                .type(TransactionType.ADDING)
                .build())).isEqualTo(true);
    }
}