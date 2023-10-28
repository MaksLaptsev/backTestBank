package com.maksru2009.utils.accrualInterest;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReadPercentagesYmlTest {
    private ReadPercentagesYml percentagesYml;

    @BeforeEach
    void setUp() {
        percentagesYml = new ReadPercentagesYml();
    }

    @Test
    void getPercent() {
        assertThat(percentagesYml.getPercent()).isNotZero();
    }
}