package com.maksru2009.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.maksru2009.builder.UserBuilder;
import com.maksru2009.entity.User;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.*;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserDaoImplTest {

    @InjectMocks
    private UserDaoImpl userDao;
    @Mock
    private Connection con;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @Test
    @SneakyThrows
    void getById() {
        String sql = "select * from banktest.User where id =?";
        User expected = UserBuilder.sUser().build();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doNothing().when(preparedStatement).setInt(1,expected.getId());
        Mockito.doReturn(resultSet).when(preparedStatement).executeQuery();
        Mockito.doReturn(true).when(resultSet).next();

        mockResulSetUser(expected);
        User actual = userDao.getById(expected.getId());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    void getAll() {
        String sql = "select * from banktest.User";
        User user = UserBuilder.sUser().build();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doReturn(resultSet).when(preparedStatement).executeQuery();
        Mockito.doReturn(true,true,false).when(resultSet).next();

        mockResulSetUser(user);

        List<User> expected = List.of(user,user);
        List<User> actual = userDao.getAll();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    void getAllUsersFromBank() {
        String sql = "select u.id as user_id, u.name as user_name, u.lastname as user_lastname," +
                "u.secondname as user_secondname, u.phonenumber as user_phonenumber from banktest.User u " +
                "inner join banktest.bank_user bu on bu.user_id = u.id " +
                "where bu.bank_id = ?";
        User user = UserBuilder.sUser().build();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doNothing().when(preparedStatement).setInt(Mockito.anyInt(),Mockito.anyInt());
        Mockito.doReturn(resultSet).when(preparedStatement).executeQuery();
        Mockito.doReturn(true,false).when(resultSet).next();

        mockResulSetUser(user,false);

        List<User> expected = List.of(user);
        List<User> actual = userDao.getAllUsersFromBank(1);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    void addUser() {
        String sql = "insert into bankTest.User (name,lastname,secondname,phonenumber) VALUES (?,?,?,?)";
        User user = UserBuilder.sUser().build();
        int idExpected = user.getId();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        Mockito.doNothing().when(preparedStatement).setString(Mockito.anyInt(),Mockito.any(String.class));
        Mockito.doReturn(2).when(preparedStatement).executeUpdate();
        Mockito.doReturn(resultSet).when(preparedStatement).getGeneratedKeys();
        Mockito.doReturn(true).when(resultSet).next();
        Mockito.doReturn(user.getId()).when(resultSet).getInt(1);

        int idActual = userDao.addUser(user);

        assertThat(idActual).isEqualTo(idExpected);
    }

    @Test
    @SneakyThrows
    void addUserInBank() {
        String sql = "insert into banktest (bank_id,user_id) VALUES (?,?)";
        User user = UserBuilder.sUser().build();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);

        //for userDao.addUser()
        sql = "insert into bankTest.User (name,lastname,secondname,phonenumber) VALUES (?,?,?,?)";
        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        Mockito.doNothing().when(preparedStatement).setString(Mockito.anyInt(),Mockito.any(String.class));
        Mockito.doReturn(resultSet).when(preparedStatement).getGeneratedKeys();
        Mockito.doReturn(true).when(resultSet).next();
        Mockito.doReturn(user.getId()).when(resultSet).getInt(1);

        Mockito.doNothing().when(preparedStatement).setInt(Mockito.anyInt(),Mockito.anyInt());
        Mockito.doReturn(2).when(preparedStatement).executeUpdate();

        userDao.addUserInBank(user,1);

        Mockito.verify(preparedStatement,Mockito.times(2)).executeUpdate();
    }

    @Test
    @SneakyThrows
    void updateUser() {
        String sql = "update banktest.user set (name,lastname,secondname,phonenumber) values (?,?,?,?) where id = ?";
        User user = UserBuilder.sUser().build();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doNothing().when(preparedStatement).setString(Mockito.anyInt(),Mockito.any(String.class));
        Mockito.doNothing().when(preparedStatement).setInt(Mockito.anyInt(),Mockito.anyInt());
        Mockito.doReturn(2).when(preparedStatement).executeUpdate();

        userDao.updateUser(user);

        Mockito.verify(preparedStatement).executeUpdate();
    }

    @Test
    @SneakyThrows
    void deleteUserById() {
        String sql = "delete from banktest.user where id = ?";
        User user = UserBuilder.sUser().build();

        Mockito.doReturn(preparedStatement).when(con).prepareStatement(sql);
        Mockito.doNothing().when(preparedStatement).setInt(Mockito.anyInt(),Mockito.anyInt());
        Mockito.doReturn(2).when(preparedStatement).executeUpdate();

        userDao.deleteUserById(user.getId());

        Mockito.verify(preparedStatement).executeUpdate();
    }

    private void mockResulSetUser(User user) throws SQLException {
        mockResulSetUser(user,true);
    }
    private void mockResulSetUser(User user,boolean b) throws SQLException {
        if (b){
            Mockito.doReturn(user.getId()).when(resultSet).getInt("id");
            Mockito.doReturn(user.getName()).when(resultSet).getString("name");
            Mockito.doReturn(user.getLastName()).when(resultSet).getString("lastname");
            Mockito.doReturn(user.getSecondName()).when(resultSet).getString("secondname");
            Mockito.doReturn(user.getPhoneNumber()).when(resultSet).getString("phonenumber");
        }else {
            Mockito.doReturn(user.getId()).when(resultSet).getInt("user_id");
            Mockito.doReturn(user.getName()).when(resultSet).getString("user_name");
            Mockito.doReturn(user.getLastName()).when(resultSet).getString("user_lastname");
            Mockito.doReturn(user.getSecondName()).when(resultSet).getString("user_secondname");
            Mockito.doReturn(user.getPhoneNumber()).when(resultSet).getString("user_phonenumber");
        }
    }
}