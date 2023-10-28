package com.maksru2009.dao.impl;

import com.maksru2009.DBconnection.GetConnection;
import com.maksru2009.dao.UserDao;
import com.maksru2009.entity.Bank;
import com.maksru2009.entity.User;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao<User> {
    private  Connection con;

    public UserDaoImpl(){
        con = GetConnection.getConnection();
    }

    /**
     * Метод для получения {@link User} из БД
     * @param id - id необходимого {@link User}
     * @return - {@link User}
     * @throws SQLException
     */
    @Override
    public User getById(int id) throws SQLException {
        String sql = "select * from banktest.User where id =?";
        User user = null;
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setInt(1,id);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                user = User.builder()
                        .id(set.getInt("id"))
                        .name(set.getString("name"))
                        .lastName(set.getString("lastname"))
                        .secondName(set.getString("secondname"))
                        .phoneNumber(set.getString("phonenumber"))
                        .build();
            }
        }
        return Optional.ofNullable(user).orElseGet(User::new);
    }

    /**
     * Метод для получения списка всех {@link User} из БД
     * @return - список {@link User}
     * @throws SQLException -
     */
    @Override
    public List<User> getAll() throws SQLException{
        List<User> userList = new ArrayList<>();
        String sql = "select * from banktest.User";
        try(PreparedStatement statement = con.prepareStatement(sql)){
            ResultSet set = statement.executeQuery();
            while (set.next()){
                userList.add(User.builder()
                        .id(set.getInt("id"))
                        .name(set.getString("name"))
                        .lastName(set.getString("lastname"))
                        .secondName(set.getString("secondname"))
                        .phoneNumber(set.getString("phonenumber"))
                        .build());
            }
        }
        return userList;
    }

    /**
     * Метод для получения списка {@link User}, пренадлежавших конкретному {@link Bank}
     * @param bankId - id {@link Bank}, на основе которого нужно получить список
     * @return - список {@link User}
     */
    @Override
    public List<User> getAllUsersFromBank (int bankId) throws SQLException {
        String sql = "select u.id as user_id, u.name as user_name, u.lastname as user_lastname," +
                "u.secondname as user_secondname, u.phonenumber as user_phonenumber from banktest.User u " +
                "inner join banktest.bank_user bu on bu.user_id = u.id " +
                "where bu.bank_id = ?";
        List<User> userListFromBank = new ArrayList<>();
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setInt(1,bankId);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                userListFromBank.add(User.builder()
                        .id(set.getInt("user_id"))
                        .name(set.getString("user_name"))
                        .lastName(set.getString("user_lastname"))
                        .secondName(set.getString("user_secondname"))
                        .phoneNumber(set.getString("user_phonenumber"))
                        .build());
            }
        }
        return userListFromBank;
    }

    /**
     * Метод для добавления экземпляра {@link User} в БД
     * @param object - экземпляр {@link User}
     * @return - int id, сохраненного {@link User}
     * @throws - в случае неудачного сохранения объекта
     */
    @Override
    public int addUser(User object) throws SQLException {
        String sql = "insert into bankTest.User (name,lastname,secondname,phonenumber) VALUES (?,?,?,?)";
        int userID;
        try(PreparedStatement statement = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1,object.getName());
            statement.setString(2,object.getLastName());
            statement.setString(3,object.getSecondName());
            statement.setString(4,object.getPhoneNumber());
            int rows = statement.executeUpdate();
            if(rows == 0){
                throw new SQLException("Creating user failed, no rows affected.");
            }
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                userID = rs.getInt(1);
            }else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
        return userID;
    }

    /**
     * Метод для построения связи по типу Many-to-Many между {@link User} и {@link Bank}
     * @param object - объект {@link User}
     * @param bankId - id объекта {@link Bank}
     * @throws - в случае неудачного создания взаимосвязи Many-to-Many
     */
    @Override
    public void addUserInBank(User object, int bankId) throws SQLException {
        String sql = "insert into banktest (bank_id,user_id) VALUES (?,?)";
        try(PreparedStatement statement = con.prepareStatement(sql)){
            int userID = addUser(object);
            statement.setInt(1,bankId);
            statement.setInt(2,userID);
            int rows = statement.executeUpdate();
            if (rows == 0){
                throw new SQLException("Creating relationship in  bank_user failed, no rows affected.");
            }
        }
    }

    /**
     * Метод для обновления информации в БД об {@link User}
     * @param object - экземпляр {@link User}, который необходимо обновить
     * @throws SQLException - в случае неудачного обновления информации в БД
     */
    @Override
    public void updateUser(User object) throws SQLException {
        String sql = "update banktest.user set (name,lastname,secondname,phonenumber) values (?,?,?,?) where id = ?";
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setString(1, object.getName());
            statement.setString(2, object.getLastName());
            statement.setString(3, object.getSecondName());
            statement.setString(4, object.getPhoneNumber());
            statement.setInt(5,object.getId());
            int rows = statement.executeUpdate();
            if (rows == 0){
                throw new SQLException("Updating user failed, no rows affected.");
            }
        }
    }

    /**
     * Метод для удаления {@link User} из БД
     * @param id - id {@link User}, который нужно удалить
     * @throws SQLException - в случаее неудачного удаления объекта
     */
    @Override
    public void deleteUserById(int id) throws SQLException {
        String sql = "delete from banktest.user where id = ?";
        try(PreparedStatement statement = con.prepareStatement(sql)){
            statement.setInt(1,id);
            int rows = statement.executeUpdate();
            if (rows == 0){
                throw new SQLException("Deleting user failed, id not found.");
            }
        }
    }
}
