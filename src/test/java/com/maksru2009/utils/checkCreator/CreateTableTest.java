package com.maksru2009.utils.checkCreator;

import static org.assertj.core.api.Assertions.*;

import com.itextpdf.layout.element.Table;
import com.maksru2009.entity.Account;
import com.maksru2009.entity.Bank;
import com.maksru2009.entity.Transaction;
import com.maksru2009.entity.User;
import com.maksru2009.type.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

class CreateTableTest {

    private CreateTable createTable;
    private List<Transaction> transactions;
    private Account account;

    @BeforeEach
    void setUp() {
        Bank bank = Bank.builder().id(1).name("Name").build();
        User user = User.builder().phoneNumber("125151").secondName("second").lastName("last").name("name").build();
        account = Account.builder().dateOpen(new Timestamp(100000)).user(user).accountNumber("dsgfsgs").amount(124).build();
        createTable = new CreateTable();
        Transaction transaction = Transaction.builder()
                .type(TransactionType.OUTGOING)
                .amount(124)
                .timestamp(new Timestamp(new Date().getTime()))
                .sendingBank(bank)
                .beneficiaryBank(bank)
                .beneficiaryUser(user)
                .sendingUser(user)
                .sendingAccount(account)
                .beneficiaryAccount(account)
                .build();
        transactions = List.of(transaction,transaction);
    }

    @ParameterizedTest
    @MethodSource("argsForConvertTypeForExtract")
    void convertTypeForExtract(String expend, Transaction t) {
        String actual = createTable.convertTypeForExtract(t);

        assertThat(actual).isEqualTo(expend);
    }

    @ParameterizedTest
    @EnumSource(TypeOfCheck.class)
    void headingTest(TypeOfCheck type){
        Table actual = createTable.heading(type,"Bank");
        assertThat(actual).isNotNull();
    }


    @ParameterizedTest
    @EnumSource(TypeOfCheck.class)
    void tableForCheckTest(TypeOfCheck type){
        List<Table> actual = createTable.tableForCheck(transactions,type,account,new Timestamp(11111),
                new Timestamp(11111),new Timestamp(11111));

        assertThat(actual).isNotNull();
    }

    static Stream<Arguments> argsForConvertTypeForExtract(){
        return Stream.of(
                Arguments.of("Входящий перевод от Иванов",
                        Transaction.builder()
                                .type(TransactionType.INCOMING)
                                .sendingUser(User.builder()
                                        .lastName("Иванов")
                                        .build())
                                .build()),
                Arguments.of("Снятие наличных",
                        Transaction.builder()
                                .type(TransactionType.CASH)
                                .build()),
                Arguments.of("Пополнение счета",
                        Transaction.builder()
                                .type(TransactionType.ADDING)
                                .build()),
                Arguments.of("Исходящий перевод для Петров",
                        Transaction.builder()
                                .type(TransactionType.OUTGOING)
                                .beneficiaryUser(User.builder()
                                        .lastName("Петров")
                                        .build())
                                .build())
        );
    }
}