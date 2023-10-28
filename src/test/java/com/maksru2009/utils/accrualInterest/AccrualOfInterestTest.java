package com.maksru2009.utils.accrualInterest;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ExtendWith(MockitoExtension.class)
class AccrualOfInterestTest {
    @Mock
    private ScheduledExecutorService scheduledExecutorService;
    @InjectMocks
    private AccrualOfInterest accrual;

    @Test
    @SneakyThrows
    void run() {
        final ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);

        accrual.run();

        Mockito.verify(scheduledExecutorService)
                .scheduleAtFixedRate(argumentCaptor.capture(),Mockito.eq(0L),Mockito.eq(30L),
                        Mockito.eq(TimeUnit.SECONDS));
    }


    @Test
    void off() {
        accrual.off();

        Mockito.verify(scheduledExecutorService).shutdown();
    }
}