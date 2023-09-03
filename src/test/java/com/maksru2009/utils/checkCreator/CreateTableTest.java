package com.maksru2009.utils.checkCreator;

import com.maksru2009.entity.Transaction;
import com.maksru2009.entity.User;
import com.maksru2009.type.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CreateTableTest {

    private CreateTable createTable;

    @BeforeEach
    void setUp() {
        createTable = new CreateTable();
    }

    @ParameterizedTest
    @MethodSource("argsForConvertTypeForExtract")
    void convertTypeForExtract(String expend, Transaction t) {
        String actual = createTable.convertTypeForExtract(t);

        assertThat(actual).isEqualTo(expend);
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