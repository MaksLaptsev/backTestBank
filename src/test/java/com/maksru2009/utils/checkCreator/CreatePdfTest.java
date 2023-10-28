package com.maksru2009.utils.checkCreator;

import static org.assertj.core.api.Assertions.*;

import com.maksru2009.entity.Account;
import com.maksru2009.entity.Bank;
import com.maksru2009.entity.Transaction;
import com.maksru2009.entity.User;
import com.maksru2009.type.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

class CreatePdfTest {
    private CreatePdf createPdf;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        createPdf = new CreatePdf();
        Bank bank = Bank.builder().id(1).name("Name").build();
        User user = User.builder().phoneNumber("125151").secondName("second").lastName("last").name("name").build();
        Account account = Account.builder()
                .dateOpen(new Timestamp(100000))
                .user(user)
                .bank(bank)
                .accountNumber("dsgfsgs")
                .amount(124).build();
        transaction = Transaction.builder()
                .type(TransactionType.CASH)
                .id(1)
                .amount(124)
                .timestamp(new Timestamp(new Date().getTime()))
                .sendingBank(bank)
                .beneficiaryBank(bank)
                .beneficiaryUser(user)
                .sendingUser(user)
                .sendingAccount(account)
                .beneficiaryAccount(account)
                .build();
    }

    @ParameterizedTest
    @MethodSource("createRecipeArgs")
    void createRecipe(String pdfName, TypeOfCheck type,Account account1) {
        String path = System.getProperty("user.dir");
        createPdf.createRecipe(transaction,path,type,account1);
        File file = new File(pdfName);

        assertThat(file.exists()).isEqualTo(true);

        file.deleteOnExit();
    }

    static Stream<Arguments> createRecipeArgs(){
        return Stream.of(
                Arguments.of(
                        "checkNumber1.pdf",TypeOfCheck.CHECK,Account.builder()
                                .dateOpen(new Timestamp(100000))
                                .user(User.builder()
                                        .phoneNumber("125151")
                                        .secondName("second")
                                        .lastName("last")
                                        .name("name")
                                        .build())
                                .bank(Bank.builder()
                                        .name("Belveb-Bank")
                                        .build())
                                .accountNumber("dsgfsgs")
                                .amount(124).build()
                ),
                Arguments.of(
                        "statementDate"+new SimpleDateFormat("dd-MM-yyyy").format(new Date(15000))+".pdf",TypeOfCheck.STATEMENT,Account.builder()
                                .dateOpen(new Timestamp(100000))
                                .user(User.builder()
                                        .phoneNumber("125151")
                                        .secondName("second")
                                        .lastName("last")
                                        .name("name")
                                        .build())
                                .bank(Bank.builder()
                                        .name("BPS-Bank")
                                        .build())
                                .accountNumber("dsgfsgs")
                                .amount(124).build()
                ),
                Arguments.of(
                        "extractDate"+new SimpleDateFormat("dd-MM-yyyy").format(new Date(15000))+".pdf",TypeOfCheck.EXTRACT,Account.builder()
                                .dateOpen(new Timestamp(100000))
                                .user(User.builder()
                                        .phoneNumber("125151")
                                        .secondName("second")
                                        .lastName("last")
                                        .name("name")
                                        .build())
                                .bank(Bank.builder()
                                        .name("BNB-Bank")
                                        .build())
                                .accountNumber("dsgfsgs")
                                .amount(124).build()
                ),
                Arguments.of(
                        "checkNumber1.pdf",TypeOfCheck.CHECK,Account.builder()
                                .dateOpen(new Timestamp(100000))
                                .user(User.builder()
                                        .phoneNumber("125151")
                                        .secondName("second")
                                        .lastName("last")
                                        .name("name")
                                        .build())
                                .bank(Bank.builder()
                                        .name("BSB-Bank")
                                        .build())
                                .accountNumber("dsgfsgs")
                                .amount(124).build()
                ),
                Arguments.of(
                        "checkNumber1.pdf",TypeOfCheck.CHECK,Account.builder()
                                .dateOpen(new Timestamp(100000))
                                .user(User.builder()
                                        .phoneNumber("125151")
                                        .secondName("second")
                                        .lastName("last")
                                        .name("name")
                                        .build())
                                .bank(Bank.builder()
                                        .name("Bank")
                                        .build())
                                .accountNumber("dsgfsgs")
                                .amount(124)
                                .build()
                )

        );
    }
}
