package com.maksru2009.utils.checkCreator;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.maksru2009.entity.Account;
import com.maksru2009.entity.Bank;
import com.maksru2009.entity.Transaction;
import com.maksru2009.type.TransactionType;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для создания объекта типа {@link Table} для {@link CreatePdf}
 */
public class CreateTable {
    public CreateTable() {
    }

    /**
     * Создание заголовка для таблицы
     * @param type - {@link TypeOfCheck} тип печатуемого чека
     * @param ownerBank - {@link Bank} банк, в котором совершается операция
     * @return - {@link Table}
     */
    public Table heading(TypeOfCheck type,String ownerBank){
        Table table1 = new Table(560);
        table1.setMarginTop(55).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);

        switch (type){
            case CHECK -> {
                table1.addCell("Банковский чек");
            }
            case STATEMENT -> {
                table1.addCell("Выписка");
                table1.addCell(ownerBank);
            }
            case EXTRACT -> {
                table1.addCell("Money statement");
                table1.addCell(ownerBank);
            }
        }
        return table1;
    }

    /**
     * Создание экхемпляра {@link Table} на основе входящих параметров
     * @param t - список {@link Transaction} для добавления в таблицу
     * @param type - {@link TypeOfCheck} тип печатаемого чека
     * @param account - {@link Account} на котором был заказан чек
     * @param dateFrom - дата, от которого числа нужна информация об {@link Transaction}
     * @param dateFor - дата, до которого числа нужна информация об {@link Transaction}
     * @param date - дата формирования запроса на чек
     * @return - {@link Table}
     */
    public List<Table> tableForCheck(List<Transaction> t, TypeOfCheck type, Account account,Timestamp dateFrom
            , Timestamp dateFor, Timestamp date){
        switch (type){
            case CHECK -> {
                return List.of(checkTable(t.get(0)));
            }
            case EXTRACT -> {
                return extractTable(t,account, dateFrom
                        , dateFor, date);
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * @see CreateTable#tableForCheck(List, TypeOfCheck, Account, Timestamp, Timestamp, Timestamp)
     */
    private List<Table> extractTable(List<Transaction> transactions, Account account, Timestamp dateFrom
            , Timestamp dateFor, Timestamp date){
        List<Table> tables = new ArrayList<>();
        float col = 280;
        float[] columnWidth = {col,col};

        Table table = new Table(columnWidth);
        table.addCell(new Paragraph("Клиент").setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph(account.getUser().getLastName()+" "
                +account.getUser().getName()+" "+account.getUser().getSecondName())
                .setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph("Счет").setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph(account.getAccountNumber()).setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph("Валюта").setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph("BYN").setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph("Дата открытия").setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph(new SimpleDateFormat("dd/MM/yyyy").format(account.getDateOpen()))
                .setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph("Период").setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph(periodConvert(dateFrom,dateFor)).setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph("Дата и время формирования").setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date))
                .setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph("Остаток").setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph(String.valueOf(account.getAmount())+" BYN").setTextAlignment(TextAlignment.LEFT));

        Table table1 = new Table(new float[]{130f, 300f, 130f});
        table1.addCell(new Paragraph("Дата").setTextAlignment(TextAlignment.CENTER));
        table1.addCell(new Paragraph("Примечание").setTextAlignment(TextAlignment.CENTER));
        table1.addCell(new Paragraph("Сумма").setTextAlignment(TextAlignment.CENTER));
        for (Transaction t:transactions) {
            table1.addCell(new Paragraph(new SimpleDateFormat("dd-MM-yyyy").format(t.getTimestamp()))
                    .setTextAlignment(TextAlignment.LEFT));
            table1.addCell(new Paragraph(convertTypeForExtract(t)).setTextAlignment(TextAlignment.LEFT));
            table1.addCell(new Paragraph(convertMoneyView(t)).setTextAlignment(TextAlignment.LEFT));
        }

        tables.add(table);
        tables.add(table1);

        return tables;
    }

    /**
     * Создание {@link Table} для {@link TypeOfCheck#CHECK}
     * @param t - экземпляр {@link Transaction}
     * @return - {@link Table}
     */
    private Table checkTable(Transaction t){
        float col = 280;
        float[] columnWidth = {col,col};
        Table table = new Table(columnWidth);
        table.addCell(new Paragraph(" Чек:").setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph(t.getId()+" ").setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Paragraph(" "+new SimpleDateFormat("dd-MM-yyyy").format(t.getTimestamp()))
                .setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph(new SimpleDateFormat("HH:mm:ss").format(t.getTimestamp())+" ")
                .setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Paragraph(" Тип транзакции:").setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph(convertTypeToName(t.getType())+" ").setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Paragraph(" Банк отправителя:").setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph(t.getSendingBank().getName()+" ").setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Paragraph(" Банк получателя:").setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph(t.getBeneficiaryBank().getName()+" ").setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Paragraph(" Счет отправителя:").setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph(t.getSendingAccount().getAccountNumber()+" ")
                .setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Paragraph(" Счет получателя:").setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph(t.getBeneficiaryAccount().getAccountNumber()+" ")
                .setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Paragraph(" Сумма:").setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Paragraph(t.getAmount()+" ").setTextAlignment(TextAlignment.RIGHT));

        return table;
    }

    /**
     * Метод для преобразование {@link TransactionType} в читабельный вид
     */
    public String convertTypeForExtract(Transaction t){
        switch (t.getType()){
            case INCOMING -> {
                return "Входящий перевод от "+t.getSendingUser().getLastName();
            }
            case CASH -> {
                return "Снятие наличных";
            }
            case ADDING -> {
                return "Пополнение счета";
            }
            case OUTGOING -> {
                return "Исходящий перевод для "+t.getBeneficiaryUser().getLastName();
            }
        }
        return "";
    }

    /**
     * Метод преобразования суммы для печати в чек
     * @param t - {@link Transaction}
     * @return - String
     */
    private String convertMoneyView(Transaction t){
        switch (t.getType()){
            case INCOMING, ADDING -> {
                return t.getAmount()+" BYN";
            }
            case CASH, OUTGOING -> {
                return "-"+t.getAmount()+" BYN";
            }
        }
        return "";
    }

    /**
     * Преобразование {@link TransactionType} в текст для печати чека
     * @param type - {@link TransactionType}
     * @return - String
     */
    private String convertTypeToName(TransactionType type) {
        switch (type){
            case INCOMING -> {return "Входящий перевод";}
            case ADDING -> {return "Пополнение счета";}
            case OUTGOING -> {return "Исходящий перевод";}
            default -> {return "Снятие наличных";}
        }
    }

    /**
     * Метод для преобразования {@link Timestamp} в текст, формата дд/ММ/гггг, для печати чека
     * @param fromDate - {@link Timestamp} дата, начало периода
     * @param forDate - {@link Timestamp} дата, конец периода
     * @return
     */
    private String periodConvert(Timestamp fromDate, Timestamp forDate){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(new SimpleDateFormat("dd/MM/yyyy").format(fromDate))
                .append(" - ")
                .append(new SimpleDateFormat("dd/MM/yyyy").format(forDate));
        return stringBuilder.toString();
    }
}
