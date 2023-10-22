package com.maksru2009.utils.checkCreator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CreateFolderTest {
    private CreateFolder createFolder;
    @BeforeEach
    void setUp() {
        createFolder = new CreateFolder();
    }

    @ParameterizedTest
    @MethodSource("correctPath")
    void createFolder(String expend, Map<String,String> values) {
        String actual = createFolder.createFolder(TypeOfCheck.valueOf(values.get("type")),
                Integer.parseInt(values.get("userID")),
                values.get("ownerBank"),values.get("ownerAcc"));

        assertThat(actual).isEqualTo(expend);
    }

    static Stream<Arguments> correctPath(){
        String userDir = System.getProperty("user.dir");
        return Stream.of(
                Arguments.of(
                    userDir+"\\check\\user1\\Bank1\\Acc1",
                        Map.of("type","CHECK","userID","1","ownerBank","Bank1",
                                "ownerAcc","Acc1")
                ),
                Arguments.of(
                        userDir+"\\statement\\user2\\Bank2\\Acc2",
                        Map.of("type","STATEMENT","userID","2","ownerBank","Bank2",
                                "ownerAcc","Acc2")
                ),
                Arguments.of(
                        userDir+"\\extract\\user3\\Bank3\\Acc3",
                        Map.of("type","EXTRACT","userID","3","ownerBank","Bank3",
                                "ownerAcc","Acc3")
                )
        );
    }
}