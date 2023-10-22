package com.maksru2009.utils.checkCreator;

import com.maksru2009.entity.Account;
import com.maksru2009.entity.Bank;
import com.maksru2009.entity.User;

import java.io.File;

public class CreateFolder {
    private final StringBuilder stringBuilder;
    public CreateFolder() {
        stringBuilder = new StringBuilder();
    }

    /**
     * Метод для создания пути сохранения для чеков на основе параметров
     * @param type - тип создаваемого чека {@link TypeOfCheck}
     * @param userID - id пользователя {@link User}, для которого создается чек
     * @param ownerBank - id банка {@link Bank}, в котором была совершена операция
     * @param ownerAcc - id аккаунта {@link Account}, на котором совершена операция
     * @return - String
     */
    public String createFolder(TypeOfCheck type, int userID, String ownerBank,String ownerAcc){
        stringBuilder.append(System.getProperty("user.dir"));
        switch (type){
            case CHECK -> {
                stringBuilder.append("\\check").append("\\user").append(userID).append("\\").append(ownerBank)
                        .append("\\").append(ownerAcc);
                File directory = new File(stringBuilder.toString());
                if(!directory.exists()){
                    directory.mkdirs();
                }
                return stringBuilder.toString();
            }
            case EXTRACT -> {
                stringBuilder.append("\\extract").append("\\user").append(userID).append("\\").append(ownerBank)
                        .append("\\").append(ownerAcc);
                File directory = new File(stringBuilder.toString());
                if(!directory.exists()){
                    directory.mkdirs();
                }
                return stringBuilder.toString();
            }
            case STATEMENT -> {
                stringBuilder.append("\\statement").append("\\user").append(userID).append("\\").append(ownerBank)
                        .append("\\").append(ownerAcc);
                File directory = new File(stringBuilder.toString());
                if(!directory.exists()){
                    directory.mkdirs();
                }
                return stringBuilder.toString();
            }
            default -> {
                return System.getProperty("user.dir");
            }
        }
    }
}
