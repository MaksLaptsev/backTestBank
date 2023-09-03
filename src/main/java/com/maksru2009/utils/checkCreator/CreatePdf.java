package com.maksru2009.utils.checkCreator;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.maksru2009.entity.Account;
import com.maksru2009.entity.Transaction;
import com.maksru2009.service.impl.TransactionServiceImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Класс для создание PDF документа
 */
public class CreatePdf {
    private TransactionServiceImpl transactionService;
    private String path;
    /**
     * Путь, по которому лежит необходимый шрифт
     */
    private static final String FONT = "src/main/resources/DejaVuSans.ttf";
    public CreatePdf() {
        transactionService = new TransactionServiceImpl();
    }

    /**
     * @see CreatePdf#createRecipe(List, String, TypeOfCheck, Account, Timestamp, Timestamp, Timestamp)
     */
    public void createRecipe(Transaction t,String path, TypeOfCheck type,Account account) throws SQLException {
        try{
            createRecipe(List.of(t),path,type, account,new Timestamp(11111111L),
                    new Timestamp(11111111L),new Timestamp(11111111L));
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод, входная точка для создание PDF документа и его сохранения
     * @param tlist - список {@link Transaction} по которым будет создан PDF
     * @param path - путь, где будет сохранен документ
     * @param type - тип создаваемого документа
     * @param account - {@link Account}, который запросил создание документа
     */
    public void createRecipe(List<Transaction> tlist, String path, TypeOfCheck type, Account account,
                             Timestamp dateForm,Timestamp dateOf,Timestamp dateFor) throws IOException {
        path += createPath(tlist.get(0),type,dateForm);

        PdfFont pdfFont = PdfFontFactory.createFont(FONT, "cp1251", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);

        PdfReader pdfReader = new PdfReader(String.valueOf(ClassLoader.getSystemResource(takePdfSchema(account))));
        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfReader,pdfWriter);

        Document document = new Document(pdfDocument);
        pdfDocument.setDefaultPageSize(PageSize.A4);

        document.setFont(pdfFont);
        /**
         * Подзаголовок(к примеру: чек, выписка и т.д.)
         */
        Table table1 = new CreateTable().heading(type,account.getBank().getName());
        document.add(table1);
        /**
         * Получение списка с таблицами(таблицей) для пдф
         */
        List<Table> tables = new CreateTable().tableForCheck(tlist,type,account,dateOf,dateFor,dateForm);
        /**
         * Добавление таблиц в документ
         */
        for (Table tab:tables) {
            document.add(tab);
        }
        /**
         * Сохранение документа
         */
        document.close();

    }

    /**
     * Для формирования названия пдф документа
     * @param t - {@link Transaction}, по которой будет сформирован чек
     * @param type - тип чека
     * @param dateForm - дата формирования
     * @return - наименование будущего PDF документа
     */
    private String createPath(Transaction t,TypeOfCheck type,Timestamp dateForm){
        switch (type){
            case STATEMENT -> {
                return "\\statementDate"+new SimpleDateFormat("dd-MM-yyyy").format(dateForm)+".pdf";
            }
            case CHECK -> {
                return "\\checkNumber"+t.getId()+".pdf";
            }
            case EXTRACT -> {
                return "\\extractDate"+new SimpleDateFormat("dd-MM-yyyy").format(dateForm)+".pdf";
            }
        }
        return "";
    }

    /**
     * Метод для получения заготовленного шаблона в зависимости от банка
     * @param account - {@link Account}, который запросил формирование документа
     * @return - наименование файла шаблона
     */
    private String takePdfSchema(Account account){
        switch (account.getBank().getName()){
            case "Belveb-Bank" ->{
                return "BELVEB.pdf";
            }
            case "BPS-Bank" ->{
                return "BPS.pdf";
            }
            case "BNB-Bank" ->{
                return "BNB.pdf";
            }
            case "BSB-Bank" ->{
                return "BSB.pdf";
            }
            default -> {return "Clever.pdf";}
        }
    }

}
