package com.maksru2009.utils.accrualInterest;

import com.maksru2009.entity.Account;
import com.maksru2009.entity.User;
import com.maksru2009.service.impl.AccountServiceImpl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс для обработки и начисления % к остаткам на счетах {@link Account} пользователей {@link User}
 */
public class AccrualOfInterest {
    private LocalDate previousMonth;
    private final Lock lock = new ReentrantLock();
    private ExecutorService executorService;
    private ScheduledExecutorService executorServiceCheckDate;
    private AccountServiceImpl accountService;
    private List<Account> accountList;
    private  double percent;

    public AccrualOfInterest() {
    }

    /**
     * Конструктр создания объекта
     * @param pool - кол-во необходимых потоков
     */
    public AccrualOfInterest(int pool){
        accountService = new AccountServiceImpl();
        percent = new ReadPercentagesYml().getPercent();
        previousMonth = LocalDate.now();
        executorService = Executors.newFixedThreadPool(pool);
        executorServiceCheckDate = Executors.newScheduledThreadPool(1);
    }

    /**
     * Метод для запуска обработки счтов {@link Account}
     * Каждые 30 секунд будет произведена проверка, не наступил ли конец текущего месяца,
     * в случае, если наступил, то будут запущены N кол-во потоков {@link #executorService}, которые
     * проведут начисление % {@link #percent} , в ассинхронном режиме, на остатки счетов {@link Account}
     */
    public void run(){
        executorServiceCheckDate.scheduleAtFixedRate(()->{
            LocalDate date = LocalDate.now();
            if (isEndOfMonth(date) && isNewMonth(date)){
                try {
                    accountList = accountService.getAllAccounts();
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                checkingPercentages();
            }
        },0,30,TimeUnit.SECONDS);
    }

    /**
     * Метод для завершения работы {@link #executorServiceCheckDate}
     */
    public void off(){
        executorServiceCheckDate.shutdown();
    }

    /**
     * Метод для проверки остатка необработанных экземпляров {@link Account}
     */
    private void checkingPercentages(){
        while (accountList.size() > 0){
            executorService.execute(this::getAccAndUpdate);
        }
    }

    /**
     * Метод для получения {@link Account} для обработки
     */
    private void getAccAndUpdate(){
            Account account = null;
            lock.lock();
            try{
                if(accountList.size() > 0){
                    account = accountList.get(0);
                    accountList.remove(account);
                }
            }finally {
                lock.unlock();
            }
            updateAcc(account);
    }

    /**
     * Процесс начисления % на остаток счета и дальнейшее обновление информации в БД
     * @param account - {@link Account}
     */
    private void updateAcc(Account account){
        if (account != null){
            account.setAmount((double) Math.round((account.getAmount()*(1+percent/100))*1000d)/1000d);
            try {
                accountService.updateAmountAcc(account);
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Проверка наступления последнего дня текущего месяца
     * @param date - текущая дата
     * @return - boolean
     */
    private boolean isEndOfMonth(LocalDate date){
        int currentDayOfMoth = date.getDayOfMonth();
        int lastDayOfCurrMonth = date.withDayOfMonth(
                date.getMonth().length(date.isLeapYear())).getDayOfMonth();
        return currentDayOfMoth == lastDayOfCurrMonth;
    }

    /**
     * Проверка на соответствие текущего и прошлого месяцев, для избежания повторного начисления процентов
     * @param date - текущая дата
     * @return - boolean
     */
    private boolean isNewMonth(LocalDate date){
        if (!(previousMonth.getMonth() == date.getMonth())){
            previousMonth = date;
            return true;
        }else return false;
    }

}
