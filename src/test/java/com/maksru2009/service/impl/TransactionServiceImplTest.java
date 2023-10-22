package com.maksru2009.service.impl;

import com.maksru2009.dao.impl.TransactionDaoImpl;
import com.maksru2009.entity.Account;
import com.maksru2009.entity.Bank;
import com.maksru2009.entity.Transaction;
import com.maksru2009.entity.User;
import com.maksru2009.type.TransactionType;
import com.maksru2009.utils.MakeTransaction;
import com.maksru2009.utils.checkCreator.CreatePdf;
import com.maksru2009.utils.checkCreator.TypeOfCheck;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransactionServiceImplTest {
    @Mock
    private TransactionDaoImpl transactionDao;
    @Mock
    private MakeTransaction makeTransaction;
    @Mock
    private AccountServiceImpl accountService;
    @InjectMocks
    private TransactionServiceImpl transactionService;
    private List<Transaction> transactions;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        Bank bank = Bank.builder().id(1).name("Name").build();
        User user = User.builder().phoneNumber("125151").secondName("second").lastName("last").name("name").build();
        Account account = Account.builder()
                .dateOpen(new Timestamp(100000))
                .user(user)
                .bank(bank)
                .accountNumber("dsgfsgs")
                .amount(400).build();
        Account account1 = Account.builder()
                .dateOpen(new Timestamp(100000))
                .user(user)
                .bank(bank)
                .accountNumber("dsgfsgs")
                .amount(500).build();
        Transaction transaction = Transaction.builder()
                .type(TransactionType.CASH)
                .id(1)
                .amount(124)
                .timestamp(new Timestamp(new Date().getTime()))
                .sendingBank(bank)
                .beneficiaryBank(bank)
                .beneficiaryUser(user)
                .sendingUser(user)
                .sendingAccount(account)
                .beneficiaryAccount(account1)
                .build();
        transactions = new ArrayList<>(List.of(transaction,transaction));
    }

    @Test
    void getTransactionById() throws SQLException {
        Transaction transaction = transactions.get(0);
        Mockito.doReturn(transaction).when(transactionDao).getTransactionById(111);

        assertThat(transactionService.getTransactionById(111)).isEqualTo(transaction);
    }

    @Test
    void getTransactionFromAccount() throws SQLException {
        Mockito.doReturn(transactions).when(transactionDao).getTransactionFromAccount(1);

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

    @Test
    void addTransactionTest() throws SQLException {
        Mockito.doReturn(true).when(makeTransaction).checkAndFixTransaction(transactions.get(0));
        Mockito.doReturn(1).when(transactionDao).saveTransaction(Mockito.any(Transaction.class),Mockito.anyInt());
        Mockito.doReturn(transactions.get(0)).when(transactionDao).getTransactionById(Mockito.anyInt());

        int actual = transactionService.addTransaction(transactions.get(0));
        int expected = 1;

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getTransactionBetweenDateTest() throws SQLException {
        Mockito.doReturn(transactions.get(0).getSendingAccount()).when(accountService).getById(Mockito.anyInt());
        Mockito.doReturn(transactions).when(transactionDao)
                .getTransactionBetweenDate(Mockito.any(Timestamp.class),Mockito.any(Timestamp.class),Mockito.anyInt());

        List<Transaction> expected = transactions;

        assertThat(transactionService.getTransactionBetweenDate(new Timestamp(15131),new Timestamp(15131),1))
                .isEqualTo(expected);
    }
}
