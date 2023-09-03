package com.maksru2009.service.impl;

import com.maksru2009.dao.impl.TransactionDaoImpl;
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
import java.util.List;
import static org.assertj.core.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionServiceImplTest {
    @Mock
    private TransactionDaoImpl transactionDao;
    @InjectMocks
    private TransactionServiceImpl transactionService;
    private List<Transaction> transactions;

    @BeforeAll
    void setUp(){
        transactionDao = Mockito.mock(TransactionDaoImpl.class);
        transactions = List.of(Transaction.builder()
                .id(1)
                .type(TransactionType.INCOMING)
                .amount(555)
                .build(), Transaction.builder()
                .id(2)
                .type(TransactionType.OUTGOING)
                .amount(5667)
                .build());
    }

    @Test
    void getTransactionById() throws SQLException {
        Transaction transaction = transactions.get(0);
        Mockito.when(transactionDao.getTransactionById(111)).thenReturn(transaction);

        assertThat(transactionService.getTransactionById(111)).isEqualTo(transaction);
    }

    @Test
    void getTransactionFromAccount() throws SQLException {
        Mockito.when(transactionDao.getTransactionFromAccount(1)).thenReturn(transactions);

        assertThat(transactionService.getTransactionFromAccount(1)).isEqualTo(transactions);
    }


    @Test
    void deleteTransactionById() throws SQLException {
        transactionService.deleteTransactionById(111);

        Mockito.verify(transactionDao).deleteTransactionById(111);
    }

    @Test
    void updateTransaction() throws SQLException {
        transactionService.updateTransaction(transactions.get(1));

        Mockito.verify(transactionDao).updateTransaction(transactions.get(1));
    }
}