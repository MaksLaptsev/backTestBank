package com.maksru2009.dao.impl;

import com.maksru2009.DBconnection.GetConnection;
import com.maksru2009.dao.BankDao;
import com.maksru2009.entity.Bank;
import com.maksru2009.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BankDaoImpl implements BankDao<Bank> {
    private Connection con;

    public BankDaoImpl() {
        con = GetConnection.getConnection();
    }

    /**
     * Метод для получения {@link Bank} из БД
     * @param id - id {@link Bank} который необходимо получить
     * @return - экземпляр {@link Bank}
     * @throws  - в случае, если объект не найден
     */
    @Override
    public Bank getById(int id) throws SQLException {
        String sql = "select from banktest.bank where id = ?";
        Bank bank = null;
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setInt(1,id);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                bank = Bank.builder()
                        .id(set.getInt("id"))
                        .name(set.getString("name"))
                        .build();
            }else {throw new SQLException("No bank with this id = "+id+" was found.");}
        }
        return Optional.ofNullable(bank).orElseGet(Bank::new);
    }

    /**
     * Метод для сохранения объекта {@link Bank} в БД
     * @param object - экземпляр {@link Bank}, который необходимо сохранить
     * @return - int id сохранненого объекта
     * @throws SQLException - в случае неудачного сохранения объекта
     */
    @Override
    public int addBank(Bank object) throws SQLException {
        String sql = "insert into banktest.bank (name) values (?)";
        int bankID;
        try(PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1,object.getName());
            int rows = statement.executeUpdate();
            if(rows == 0){
                throw new SQLException("Creating Bank failed, no rows affected.");
            }
            ResultSet set = statement.getGeneratedKeys();
            if(set.next()){
                bankID = set.getInt(1);
            }else {throw new SQLException("Creating Bank failed, no ID obtained.");}
        }
        return bankID;
    }

    /**
     * Метод для получения списка всех {@link Bank} из БД
     * @return - список объектов {@link Bank}
     */
    @Override
    public List<Bank> getAllBanks() throws SQLException {
        String sql = "select * from banktest.bank";
        List<Bank> bankList = new ArrayList<>();
        try(PreparedStatement statement = con.prepareStatement(sql);
            ResultSet set = statement.executeQuery()){
            while (set.next()){
                bankList.add(Bank.builder()
                        .id(set.getInt("id"))
                        .name(set.getString("name"))
                        .build());
            }
        }
        return bankList;
    }

    /**
     * Метод для обновления информации об {@link Bank} в БД
     * @param object - объект {@link Bank}
     * @throws SQLException - в случае неудачного обновления объекта
     */
    @Override
    public void updateBank(Bank object) throws SQLException {
        String sql = "update banktest.bank set name = ? where id = ?";
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setString(1, object.getName());
            statement.setInt(2,object.getId());
            int rows = statement.executeUpdate();
            if (rows == 0){
                throw new SQLException("Updating Bank failed, no rows affected. \n" +
                        "Bank with ID = "+object.getId()+" not found");
            }
        }
    }

    /**
     * Метод для удаления объекта {@link Bank} из БД
     * @param id - id объекта {@link Bank}, который необходимо удалить
     * @throws SQLException - в случае неудачного удаления объекта/либо он не найден
     */
    @Override
    public void deleteBankById(int id) throws SQLException {
        String sql = "delete from banktest.bank where id = ?";
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setInt(1,id);
            int rows = statement.executeUpdate();
            if(rows == 0){
                throw new SQLException("Deleting Bank failed, no rows affected. \n" +
                        "Bank with ID = "+id+" not found");
            }
        }
    }

    /**
     * Метод для получения списка {@link Bank} которые есть у {@link User}
     * @param userId - id {@link User}, на основе которого необходимо получить список {@link Bank}
     * @return - список объектов {@link Bank}
     */
    @Override
    public List<Bank> getBanksByUserId(int userId) throws SQLException {
        String sql ="select b.id as bank_id, b.name as bank_name from banktest.bank b " +
                "inner join banktest.bank_user bu on b.id = bu.bank_id " +
                "where bu.user_id = ?";
        List<Bank> bankList = new ArrayList<>();
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setInt(1,userId);
            try(ResultSet set = statement.executeQuery()){
                while (set.next()){
                    bankList.add(Bank.builder()
                            .id(set.getInt("bank_id"))
                            .name(set.getString("bank_name"))
                            .build());
                }
            }
        }
        return bankList;
    }
}
