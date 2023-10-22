package com.maksru2009.consoleApp;

import com.maksru2009.entity.Account;
import com.maksru2009.entity.Bank;
import com.maksru2009.entity.Transaction;
import com.maksru2009.entity.User;
import com.maksru2009.service.impl.AccountServiceImpl;
import com.maksru2009.service.impl.BankServiceImpl;
import com.maksru2009.service.impl.TransactionServiceImpl;
import com.maksru2009.service.impl.UserServiceImpl;
import com.maksru2009.type.TransactionType;
import com.maksru2009.utils.accrualInterest.AccrualOfInterest;
import com.maksru2009.utils.checkCreator.CreateFolder;
import com.maksru2009.utils.checkCreator.CreateTable;
import com.maksru2009.utils.checkCreator.TypeOfCheck;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Класс консольного приложения, где будет происходить ветвление по пунктам меню
 */
public class ConsoleApp {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    private boolean entryPointBank = false;
    private boolean entryPointUsers = false;
    private String dateFrom="";
    private String dateFor="";
    private final Scanner sc;
    private User user;
    private User benefUser;
    private Bank bank;
    private Bank benefBank;
    private Account account;
    private Account benefAccount;
    private List<Transaction> transactions;
    private final UserServiceImpl userService;
    private final BankServiceImpl bankService;
    private final AccountServiceImpl accountService;
    private final TransactionServiceImpl transactionService;
    private final AccrualOfInterest accrual;

    /**
     * Конструктор, после создания которого инициализируется запуск подсчета за % остатка на счетах {@link AccrualOfInterest},
     * а так же запускается основная логика
     */
    public ConsoleApp() {
        sc = new Scanner(System.in);
        userService = new UserServiceImpl();
        bankService = new BankServiceImpl();
        transactionService = new TransactionServiceImpl();
        accountService = new AccountServiceImpl();
        accrual =  new AccrualOfInterest(10);
        accrual.run();
    }

    /**
     * Запуск приложения, первое меню выбора
     */
    public void startApp(){
        int choice;
        choice = mainMenu();
        switch (choice) {
            case 1 ->
                firstMenu("Banks");

            case 2 ->
                firstMenu("Users");

            case 3 ->{
                accrual.off();
                System.out.println("Good bye");
            }

            default -> {
                System.out.println("Unknown menu item\n");
                startApp();
            }
        }
    }

    /**
     * Первое ветвление в циклах
     * Выбор точки входа: Через списко банков либо список пользователей
     * И вариант выхода из приложения, с последующей остановкой работы {@link AccrualOfInterest}
     */
    private int mainMenu(){
        int choice;
        System.out.println("Welcome to the bank.....");
        System.out.println("Please select the menu item.....");
        System.out.println("1 - Show a list of available banks.");
        System.out.println("2 - Show the list of users");
        System.out.println("3 - Exit from app");

        choice = scNextInt();

        return choice;
    }

    /**
     * Исходя из предыдущего выбора будет отображен список либо всех доступных банков,
     * либо всех зарегистрированных пользователей
     * @param s - выбранный в предыдущем меню параметр
     */
    private void firstMenu(String s) {
        List<Bank> banks = new ArrayList<>();
        List<User> users = new ArrayList<>();
        switch (s) {
            case "Banks" -> {
                try {
                    int i = 0;
                    banks = bankService.getAllBanks();
                    System.out.println("List of all banks.");
                    for (Bank b : banks) {
                        System.out.println(++i + " - " + b.getName());
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("0 - Previous menu");
                System.out.println("Make a choice");
                int choice = scNextInt();
                if(choice > banks.size() || choice < 0){
                    System.out.println("\nInvalid number, try again\n");
                    firstMenu("Banks");
                } else if (choice == 0) {
                    startApp();
                }else{
                    bank = banks.get(choice-1);
                    entryPointBank = true;
                    selectedBank();
                }

            }
            case "Users" -> {
                try {
                    int i = 0;
                    users = userService.getAll();
                    System.out.println("List of all clients in all banks.");
                    for (User u : users) {
                        System.out.printf("%s - Имя: %s, Фамилия: %s, Отчество: %s, Номер телефона: %s%n",++i,
                                u.getName(),u.getLastName(),u.getSecondName(),u.getPhoneNumber());
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("0 - Previous menu");
                System.out.println("Make a choice");
                int choice = scNextInt();
                if(choice > users.size() || choice < 0){
                    System.out.println("\nInvalid number, try again\n");
                    firstMenu("Users");
                } else if (choice == 0) {
                    startApp();
                } else {
                    user = users.get(choice-1);
                    entryPointBank = false;
                    selectedUser();
                }
            }
        }
    }

    /**
     * В случае выбора списка банков в предыдущем меню, и выборе конкретного - будет предложено
     * отображение списка пользователей, которые исеют в данном банке счета
     * 0 - возвращение в предыдущее меню
     */
    private void selectedBank() {
        System.out.println("You are in bank: "+bank.getName());
        System.out.println("1 - Show the list of bank users");
        System.out.println("0 - Previous menu");
        System.out.println("Make a choice");
        int choice = scNextInt();

        switch (choice){
            case 1 ->
                showUsersFromSelectedBank();
            case 0 ->{
                bank = null;
                entryPointBank = false;
                firstMenu("Banks");
            }
            default -> {
                System.out.println("\nInvalid number, try again\n");
                selectedBank();
            }
        }
    }

    /**
     * В случае выбора всего списка пользователей из конкретного банка, {@link ConsoleApp#selectedBank()}
     * отображение списка всех пользователей этого банка
     */
    private void showUsersFromSelectedBank()  {
        System.out.println("You are in bank: "+bank.getName());
        System.out.println("Select the user you want to continue working under.\n");
        List<User> users = new ArrayList<>();
        try{
            users = userService.getAllUsersFromBank(bank.getId());
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        int choice ;
        int i = 0;
        for (User u: users) {
            System.out.printf("%s - %s %s %s\n",++i,u.getLastName(),u.getName(),u.getSecondName());
        }
        System.out.println("0 - Previous menu");
        System.out.println("Make a choice");
        choice = scNextInt();
        if(choice < 0 || choice > users.size()){
            System.out.println("\nInvalid number, try again\n");
            showUsersFromSelectedBank();
        } else if (choice == 0) {
            selectedBank();
        }else {
            user = users.get(choice-1);
            selectedUserAndBank();
        }
    }

    /**
     * В случае выбора всего списка пользователей в {@link ConsoleApp#firstMenu(String)}
     * и выборе конткретного пользователя из списка - будет отображен список банков, в которых у
     * выбранного пользователя есть счета
     */
    private void selectedUser(){
        System.out.println("Hello");
        System.out.printf("%s %s \n",user.getName(),user.getSecondName());
        System.out.println("Choose the bank you will work with.");
        try{
            List<Bank> banks = bankService.getBanksByUserId(user.getId());
            int i = 0;
            for (Bank b:banks) {
                System.out.printf("%s - %s\n",++i,b.getName());
            }
            System.out.println("0 - Previous menu");
            int choice = scNextInt();
            if(choice > banks.size() || choice < 0){
                System.out.println("\n!!!! Invalid number, try again !!!!\n");
                selectedUser();
            }else if(choice == 0){
                user = null;
                firstMenu("Users");
            }else {
                bank = banks.get(choice-1);
                selectedUserAndBank();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            selectedUser();
        }
    }

    /**
     * После выбора рабочего банка и пользователя, будет предложен список доступных пользователю счетов в этом банке
     * точки входа - {@link ConsoleApp#showUsersFromSelectedBank()} and {@link ConsoleApp#selectedUser()}
     */
    private void selectedUserAndBank(){
        List<Account> accounts = new ArrayList<>();
        int i = 0;
        System.out.printf("User: %s  %s  %s \nBank: %s \n",user.getLastName(), user.getName(), user.getSecondName(),
                bank.getName());
        System.out.println("Please choice your work account");
        try{
            accounts = accountService.getAccountsByUserIdAndBankId(user.getId(), bank.getId());
        }catch (SQLException e){
            System.out.println(e.getMessage());
            selectedUserAndBank();
        }
        for (Account a:accounts) {
            System.out.printf("%s - Номер счета: %s, Баланс: %s $\n",++i,a.getAccountNumber(),a.getAmount());
        }
        System.out.println("0 - Previous menu");
        int choice = scNextInt();
        if(choice > accounts.size() || choice < 0){
            System.out.println("\n!!!! Invalid number, try again !!!!\n");
            selectedUserAndBank();
        }else if(choice == 0){
            if(entryPointBank){
                showUsersFromSelectedBank();
            }else {bank = null;selectedUser();}
        }else {
            account = accounts.get(choice-1);
            selectedUserAndBankAndAcc();
        }
    }

    /**
     * После выбора Банка, Пользователя и его рабочего Счета будет предложен список возможных операции по счету
     * точки входа - {@link ConsoleApp#selectedUserAndBank()}
     */
    private void selectedUserAndBankAndAcc(){
        System.out.printf("User: %s  %s  %s \nBank: %s \nSelected account: %s, %s $\n",user.getLastName(), user.getName(), user.getSecondName(),
                bank.getName(),account.getAccountNumber(),account.getAmount());
        System.out.println("1 - View the list of transactions.");
        System.out.println("2 - Make a transaction on a bank account.");
        System.out.println("3 - Top up your balance.");
        System.out.println("4 - Withdrawal of funds.");
        System.out.println("5 - Get an account statement.");
        System.out.println("0 - Previous menu");
        int choice = scNextInt();
        int choose;
        switch (choice){
            case 1 -> {
                listTransactionFromAcc();
                System.out.println("Choice number for more details about transaction");
                System.out.println("0 - Previous menu");
                choose = scNextInt();
                if (choose > transactions.size() || choose <0){
                    System.out.println("\n!!!! Invalid number, try again !!!!\n");
                    selectedUserAndBankAndAcc();
                } else if (choose == 0) {
                    account = null;
                    selectedUserAndBank();
                }else {
                    detailViewTransaction(transactions.get(choose-1));
                    System.out.println("0 - Previous menu");
                    int choosing = scNextInt();
                    if (choosing == 0){
                        selectedUserAndBankAndAcc();
                    }else {
                        System.out.println("\n!!!! Invalid number, try again !!!!\n");
                    }
                }
            }
            case 2 ->
                makeTransaction();
            case 3 ->
                addMoneyToAcc();
            case 4 ->
                    withdrawalFounds();
            case 5 ->
                    getAccStatement();
            case 0 -> {
                account = null;
                selectedUserAndBank();
            }
            default -> {
                System.out.println("\n!!!! Invalid number, try again !!!!\n");
                selectedUserAndBankAndAcc();
            }
        }
    }

    /**
     * При выборе операции по переводу денежных средств - будет отображенно данное меню,
     * где предложат выбрать получателя платежа из конкретного банка и его пользователей/ либо наоболрот
     * точка входа - {@link ConsoleApp#selectedUserAndBankAndAcc()}
     */
    private void makeTransaction(){
        System.out.printf("User: %s  %s  %s \nBank: %s \nSelected account: %s, %s $\n",user.getLastName(), user.getName(), user.getSecondName(),
                bank.getName(),account.getAccountNumber(),account.getAmount());
        System.out.println("1 - Select a recipient from the list of all users.");
        System.out.println("2 - Select a user from a specific bank.");
        System.out.println("0 - Previous menu");

        int choice = scNextInt();
        switch (choice){
            case 1 -> {
                entryPointUsers = true;
                selectBenefUserFromAll();
            }
            case 2 -> {
                entryPointUsers = false;
                listBenefBanks();
            }
            case 0 ->
                selectedUserAndBankAndAcc();

            default -> {
                System.out.println("\n!!!! Invalid number, try again !!!!\n");
                makeTransaction();
            }
        }
    }

    /**
     * Выбор банка, пользователю которого будет назначатся перевод
     * точка входа - {@link ConsoleApp#makeTransaction()}
     */
    private void listBenefBanks(){
        System.out.printf("User: %s  %s  %s \nBank: %s \nSelected account: %s, %s $\n",user.getLastName(), user.getName(), user.getSecondName(),
                bank.getName(),account.getAccountNumber(),account.getAmount());
        System.out.println("Select the recipient's bank.");
        List<Bank> banks = new ArrayList<>();
        try {
            banks = bankService.getAllBanks();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            listBenefBanks();
        }
        int i = 0;
        for (Bank b: banks) {
            System.out.printf("%s - %s\n",++i,b.getName());
        }
        System.out.println("0 - Previous menu");
        System.out.println("Make a choice.");
        int choice = scNextInt();
        if(choice < 0 || choice > banks.size()){
            System.out.println("\n!!!! Invalid number, try again !!!!\n");
            listBenefBanks();
        } else if (choice == 0) {
            makeTransaction();
        } else {
            benefBank = banks.get(choice-1);
            listBenefUsers();
        }
    }

    /**
     * Выбор пользователя - получателя платежа, на основе выбора банка в {@link ConsoleApp#listBenefBanks()}
     */
    private void listBenefUsers(){
        System.out.printf("User: %s  %s  %s \nBank: %s \nSelected account: %s, %s $\n",user.getLastName(), user.getName(), user.getSecondName(),
                bank.getName(),account.getAccountNumber(),account.getAmount());
        System.out.println("Recipient Bank - "+benefBank.getName());
        System.out.println("Select the recipient's user.\n");

        List<User> users = new ArrayList<>();
        try{
            users = userService.getAllUsersFromBank(benefBank.getId());
        }catch (SQLException e){
            System.out.println(e.getMessage());
            listBenefUsers();
        }
        int i = 0;
        for (User u :users) {
            System.out.printf("%s - %s %s %s\n",++i,u.getLastName(),u.getName(),u.getSecondName());
        }
        System.out.println("0 - Previous menu");
        System.out.println("Make a choice.");
        int choice = scNextInt();
        if (choice < 0 || choice > users.size()){
            System.out.println("\n!!!! Invalid number, try again !!!!\n");
            listBenefUsers();
        } else if (choice == 0) {
            benefBank = null;
            listBenefBanks();
        }else {
            benefUser = users.get(choice-1);
            listBenefAcc();
        }
    }

    /**
     * Был выбран банк, пользователь. Выбор счета пользователя, на который будет отправлен перевод
     * точка входа {@link ConsoleApp#selectBenefBank()} and {@link ConsoleApp#listBenefUsers()}
     */
    private void listBenefAcc(){
        System.out.printf("User: %s  %s  %s \nBank: %s \nSelected account: %s, %s $\n",user.getLastName(),
                user.getName(), user.getSecondName(),
                bank.getName(),account.getAccountNumber(),account.getAmount());
        System.out.println("Recipient Bank - "+benefBank.getName());
        System.out.printf("Recipient's user - %s %s %s \n",benefUser.getLastName(),benefUser.getName(),
                benefUser.getSecondName());
        System.out.println("Selected recipient's account.\n");
        List<Account> accounts = new ArrayList<>();
        try {
            accounts = accountService.getAccountsByUserIdAndBankId(benefUser.getId(), benefBank.getId());
        }catch (SQLException e){
            System.out.println(e.getMessage());
            listBenefAcc();
        }
        int i = 0;
        for (Account a:accounts) {
            System.out.printf("%s - %s\n",++i,a.getAccountNumber());
        }
        System.out.println("0 - Previous menu");
        System.out.println("Make a choice.");
        int choice = scNextInt();
        if (choice < 0 || choice > accounts.size()){
            System.out.println("\n!!!! Invalid number, try again !!!!\n");
            listBenefAcc();
        } else if (choice == 0) {
            if (entryPointUsers){
                benefBank = null;
                selectBenefBank();
            }else {
                benefUser = null;
                listBenefUsers();
            }
        }else {
            benefAccount = accounts.get(choice-1);
            choiceAmountForTransaction();
        }
    }

    /**
     * Был выбран банк, пользователь, счет. Выбор суммы для перевода.
     * точка входа  - {@link ConsoleApp#listBenefAcc()}
     */
    private void choiceAmountForTransaction(){
        System.out.printf("User: %s  %s  %s \nBank: %s \nSelected account: %s, %s $\n",user.getLastName(),
                user.getName(), user.getSecondName(),
                bank.getName(),account.getAccountNumber(),account.getAmount());
        System.out.println("Recipient Bank - "+benefBank.getName());
        System.out.printf("Recipient's user - %s %s %s\n",benefUser.getLastName(),benefUser.getName(),
                benefUser.getSecondName());
        System.out.println("Recipient's account - " + benefAccount.getAccountNumber());
        System.out.println("Enter the amount to transfer...");
        System.out.println("Or enter 0 to cancel.\n");

        double choice = scNextDouble();
        if (choice < 0d){
            System.out.println("\n !!!! The amount to transfer cannot be a negative value !!!!\n");
            choiceAmountForTransaction();
        }else if (choice == 0d){
            benefAccount = null;
            listBenefAcc();
        }else {
            try {
                fixTransaction(Transaction.builder()
                        .amount(choice)
                        .sendingUser(user)
                        .sendingBank(bank)
                        .sendingAccount(account)
                        .beneficiaryUser(benefUser)
                        .beneficiaryBank(benefBank)
                        .beneficiaryAccount(benefAccount)
                        .type(TransactionType.OUTGOING)
                        .build());
            }catch (SQLException e){
                System.out.println(e.getMessage()+"\n");
                choiceAmountForTransaction();
            }
        }
    }

    /**
     * Выбор пользователя- получателя перевода, из списка всех доступных пользователей
     * точка входа - {@link ConsoleApp#makeTransaction()}
     */
    private void selectBenefUserFromAll(){
        System.out.printf("User: %s  %s  %s \nBank: %s \nSelected account: %s, %s $\n",user.getLastName(), user.getName(), user.getSecondName(),
                bank.getName(),account.getAccountNumber(),account.getAmount());
        System.out.println("Select the recipient's user.\n");
        List<User> users = new ArrayList<>();
        try{
            users = userService.getAll();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        int i = 0;
        for (User u:users) {
            System.out.printf("%s - %s %s %s \n",++i,u.getLastName(),u.getName(),u.getSecondName());
        }
        System.out.println("0 - Previous menu");
        System.out.println("Make a choice.");
        int choice = scNextInt();
        if(choice < 0 || choice>users.size()){
            System.out.println("\n!!!! Invalid number, try again !!!!\n");
            selectBenefUserFromAll();
        } else if (choice == 0) {
            makeTransaction();
        }else {
            benefUser = users.get(choice-1);
            selectBenefBank();
        }
    }

    /**
     * Выбор банка - получателя платежа, из доступных банков у пользователя,
     * которого выбрали на предыдущем этапе {@link ConsoleApp#selectBenefUserFromAll()}
     */
    private void selectBenefBank(){
        System.out.printf("User: %s  %s  %s \nBank: %s \nSelected account: %s, %s $\n",user.getLastName(), user.getName(), user.getSecondName(),
                bank.getName(),account.getAccountNumber(),account.getAmount());
        System.out.printf("Recipient's user - %s %s %s\n",benefUser.getLastName(),benefUser.getName(),
                benefUser.getSecondName());
        System.out.println("Select the recipient's bank.\n");
        List<Bank> banks = new ArrayList<>();
        try{
            banks = bankService.getBanksByUserId(benefUser.getId());
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        int i = 0;
        for (Bank b:banks) {
            System.out.printf("%s - %s \n",++i,b.getName());
        }
        System.out.println("0 - Previous menu");
        System.out.println("Make a choice.");
        int choice = scNextInt();
        if (choice < 0 || choice > banks.size()){
            System.out.println("\n!!!! Invalid number, try again !!!!\n");
        } else if (choice == 0) {
            benefUser = null;
            selectBenefUserFromAll();
        }else {
            benefBank = banks.get(choice-1);
            listBenefAcc();
        }
    }

    /**
     * Пункт из меню операций {@link ConsoleApp#selectedUserAndBankAndAcc()} для снятия денежныъ средств
     */
    private void withdrawalFounds(){
        System.out.printf("User: %s  %s  %s \nBank: %s \nSelected account: %s, %s $\n",user.getLastName(), user.getName(), user.getSecondName(),
                bank.getName(),account.getAccountNumber(),account.getAmount());
        System.out.println("Specify the amount you want to withdraw.\n");
        System.out.println("0 - Previous menu");

        double choice = scNextDouble();
        if (choice < 0d){
            System.out.println("\n!!!! The amount to transfer cannot be a negative value !!!!\n");
            withdrawalFounds();
        } else if (choice == 0d) {
            selectedUserAndBankAndAcc();
        }else {
            try {
                fixTransaction(Transaction.builder()
                        .amount(choice)
                        .sendingUser(user)
                        .sendingBank(bank)
                        .sendingAccount(account)
                        .beneficiaryUser(user)
                        .beneficiaryBank(bank)
                        .beneficiaryAccount(account)
                        .type(TransactionType.CASH)
                        .build());
            }catch (SQLException e){
                System.out.println(e.getMessage()+"\n");
                withdrawalFounds();
            }
        }
    }

    /**
     * Пункт из меню операций {@link ConsoleApp#selectedUserAndBankAndAcc()} для пополнения счета
     */
    private void addMoneyToAcc(){
        System.out.printf("User: %s  %s  %s \nBank: %s \nSelected account: %s, %s $\n",user.getLastName(), user.getName(), user.getSecondName(),
                bank.getName(),account.getAccountNumber(),account.getAmount());
        System.out.println("Specify the amount you want to adding to account.\n");
        System.out.println("0 - Previous menu");

        double choice = scNextDouble();
        if (choice < 0d){
            System.out.println("\n!!!! The amount to transfer cannot be a negative value !!!!\n");
            withdrawalFounds();
        } else if (choice == 0d) {
            selectedUserAndBankAndAcc();
        }else {
            try {
                fixTransaction(Transaction.builder()
                        .amount(choice)
                        .sendingUser(user)
                        .sendingBank(bank)
                        .sendingAccount(account)
                        .beneficiaryUser(user)
                        .beneficiaryBank(bank)
                        .beneficiaryAccount(account)
                        .type(TransactionType.ADDING)
                        .build());
            }catch (SQLException e){
                System.out.println(e.getMessage()+"\n");
                addMoneyToAcc();
            }
        }
    }

    /**
     * После ввода суммы для совершения перевода - фиксация данной транзакции
     * точка входа {@link ConsoleApp#selectedUserAndBankAndAcc()} пункт 2
     */
    private void fixTransaction(Transaction t) throws SQLException{
            int id = transactionService.addTransaction(t);
            try{
                account = accountService.getById(account.getId());
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
            System.out.println("Transaction completed successfully, receipt number - "+id);
            alsoSaveDir(TypeOfCheck.CHECK);
            System.out.println("1 - Return to operation menu.");
            System.out.println("2 - Return to the start menu.");
            int ch = scNextInt();
            switch (ch){
                case 2 ->{
                    bank = null;
                    benefBank = null;
                    user = null;
                    benefUser = null;
                    account = null;
                    benefAccount = null;
                    entryPointBank = false;
                    startApp();
                }
                case 1 ->
                        selectedUserAndBankAndAcc();
                default -> {
                    System.out.println("\n!!!! Invalid number, try again !!!!");
                    System.out.println("!!!! Unknown selection, you will be moved to your account menu. !!!!\n");
                    selectedUserAndBankAndAcc();
                }
            }
    }

    /**
     * Пункт из меню операций {@link ConsoleApp#selectedUserAndBankAndAcc()} - получение выписки по счету за заданный период
     * меню для ввода этого периода
     */
    private void getAccStatement(){
        System.out.printf("User: %s  %s  %s \nBank: %s \nSelected account: %s, %s $\n",user.getLastName(), user.getName(), user.getSecondName(),
                bank.getName(),account.getAccountNumber(),account.getAmount());
        System.out.println("Get Statement\n");
        System.out.println("Date from: "+dateFrom);
        System.out.println("Date for: "+dateFor+"\n");
        System.out.println("1 - Enter the start date of the period");
        System.out.println("2 - Enter the end date of the period");
        System.out.println("0 - Previous menu\n");
        if (!"".equals(dateFor) && !"".equals(dateFrom)){
            System.out.println("3 - Get statement between "+dateFrom+" and "+dateFor);
        }
        int choice = scNextInt();

        switch (choice){
            case 0 ->{
                dateFor="";
                dateFrom="";
                selectedUserAndBankAndAcc();
            }
            case 1 ->{
                System.out.println("Enter the date of");
                System.out.println("Use yyyy-MM-dd format");
                dateFrom = readDate();
                getAccStatement();
            }
            case 2 ->{
                System.out.println("Enter the date for");
                System.out.println("Use yyyy-MM-dd format");
                dateFor = readDate();
                getAccStatement();
            }
            case 3 -> {
                if (!"".equals(dateFor) && !"".equals(dateFrom)){
                    printStatement(statementFromAcc());

                    System.out.println("0 - Back to account menu");
                    dateFrom = "";
                    dateFor = "";
                    int x = scNextInt();
                    if (x == 0){
                        selectedUserAndBankAndAcc();
                    }else {selectedUserAndBankAndAcc();}
                }
            }
            default -> {
                System.out.println("\n!!!! Invalid number, try again !!!!\n");
                getAccStatement();
            }
        }
    }

    /**
     * Используется для ввода даты
     * при неккоректном вводе пишет предупреждение
     */
    private String readDate(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String s = sc.next();

        try {
            df.parse(s);
            return s;
        }catch (ParseException e){
            System.out.println("\n!!!! Invalid format, example of the required one: 2002-11-22 !!!!");
            System.out.println("!!!! Try again !!!!\n");
        }
        return readDate();
    }

    /**
     * Используется для вывода в консоль чека по операциям
     */
    private void detailViewTransaction(Transaction t){
        String repeat = "-".repeat(64);
        String check = """
                       %s%s
                       | %36s%26s
                       | Чек: %55s |
                       | %s %49s |
                       | Тип транзакции: %44s |
                       | Банк отправителя: %42s |
                       | Банк получателя: %43s |
                       | Счет отправителя: %42s |
                       | Счет получателя: %43s |
                       | Сумма: %53s |
                       %s
                       """.formatted("\n",repeat,"Банковский чек","|",
                t.getId(),
                new SimpleDateFormat("dd-MM-yyyy").format(t.getTimestamp()),
                new SimpleDateFormat("HH:mm:ss").format(t.getTimestamp()),
                convertTypeToName(t.getType()),
                t.getSendingBank().getName(),
                t.getBeneficiaryBank().getName(),
                t.getSendingAccount().getAccountNumber(),
                t.getSendingAccount().getAccountNumber(),
                t.getAmount(),
                repeat);
        System.out.println(check);
        alsoSaveDir(TypeOfCheck.CHECK);
    }

    /**
     * используется для вывода в консоль выписки по операциям
     */
    private void printStatement(List<Transaction> list ){
        if (list.size() > 0){

            String repeat = "-".repeat(64);
            String check = """
                           %s%s
                           | %36s%26s
                           | %36s%26s
                           | Клиент: %32s %s %s |
                           | Счет: %54s |
                           | Валюта: %52s |
                           | Дата открытия: %45s |
                           | Период: %41s/%s |
                           | Дата и время формирования: %33s |
                           | Остаток: %51s |
                           %s
                           """.formatted("\n",repeat,"Выписка","|",bank.getName(),"|",
                    user.getLastName(),user.getName(),user.getSecondName(),
                    account.getAccountNumber(),
                    "BYN",
                    new SimpleDateFormat("dd-MM-yyyy").format(account.getDateOpen()),
                    dateFrom,dateFor,
                    new SimpleDateFormat("dd-MM-yyyy").format(new Date().getTime()),
                    account.getAmount(),
                    repeat);
            System.out.print(check);
            String ss = """
                            Дата %15s Примечание %22s Сумма
                        
                        """.formatted("|","|");
            System.out.print(ss);
            for (Transaction t: list) {
                String sss = """
                    %-22s %-1s %-31s %s %s
                    """.formatted(new SimpleDateFormat("dd-MM-yyyy").format(t.getTimestamp()),
                        "|",
                        new CreateTable().convertTypeForExtract(t),"|",t.getAmount());
                System.out.print(sss);
            }
            alsoSaveDir(TypeOfCheck.EXTRACT);
            System.out.println();
        }
    }

    /**
     * Используется для преобразования введенной даты {@link ConsoleApp#getAccStatement()}
     * в тип {@link Timestamp} для дальнейшей обработки
     */
    private List<Transaction> statementFromAcc()  {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Timestamp t1 = new Timestamp(df.parse(dateFrom).getTime());
            Timestamp t2 = new Timestamp(df.parse(dateFor).getTime());
            return transactionService.getTransactionBetweenDate(t1,t2,account.getId());
        }catch (ParseException | SQLException e){
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * Тип операции, для отображения в консоль
     */
    private String convertTypeToName(TransactionType type){
        switch (type){
            case INCOMING -> {return "Входящий перевод";}
            case ADDING -> {return "Пополнение счета";}
            case OUTGOING -> {return "Исходящий перевод";}
            default -> {return "Снятие наличных";}
        }
    }

    /**
     * Выводит данные об транзакцияъ, совершенных на счету пользователя.
     * точка входа {@link ConsoleApp#selectedUserAndBankAndAcc()} пункт 1
     */
    private void listTransactionFromAcc(){
        try{
            transactions = transactionService.getTransactionFromAccount(account.getId());
            int i = 0;
            for (Transaction t:transactions) {
                String s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(t.getTimestamp());
                System.out.printf("%s - Number transaction: %s, Date: %s, Amount: %s\n",++i,t.getId(),s,t.getAmount());
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            selectedUserAndBankAndAcc();
        }
    }

    /**
     * Используется для считывания корректного int с консоли
     */
    private int scNextInt(){
        int choice;
        try{
            choice = sc.nextInt();
            return choice;
        }catch (InputMismatchException e){
            System.out.println("\n!!!! Please make a choice using only numbers !!!!\n");
            sc.next();
        }
        return scNextInt();
    }

    /**
     * Используется для считывания корректного double с консоли
     */
    private double scNextDouble(){
        double choice;
        try{
            choice = sc.nextDouble();
            return choice;
        }catch (InputMismatchException e){
            System.out.println("!!!! Please make a choice using only numbers !!!!");
            System.out.println("!!!! And use ',' instead of '.' !!!!\n");
            sc.next();
        }
        return scNextDouble();
    }

    /**
     * ДУблирование информации об пути,для вывода в консоль, где был сохранен чек в PDF формате
     */
    private void alsoSaveDir(TypeOfCheck type){
        String path = new CreateFolder().createFolder(type,user.getId(),bank.getName(),account.getAccountNumber());
        System.out.println("\nIn pdf format you can find it in\n"+path+"\n");
    }

}
