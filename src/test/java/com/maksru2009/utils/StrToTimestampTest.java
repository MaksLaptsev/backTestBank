package com.maksru2009.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.*;

class StrToTimestampTest {
    private Timestamp timestampExtend;
    private String s;
    @BeforeEach
    void setUp() throws ParseException {
       s = "2023-09-11";
       DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        timestampExtend = new Timestamp(df.parse(s).getTime());
    }

    @Test
    void convert() throws ParseException {
        Timestamp actual = StrToTimestamp.convert(s);

        assertThat(actual).isEqualTo(timestampExtend);
    }
}