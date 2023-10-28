package com.maksru2009.service.impl;

import static org.assertj.core.api.Assertions.*;

import com.maksru2009.dao.impl.UserDaoImpl;
import com.maksru2009.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.SQLException;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceImplTest {
    @Mock
    private UserDaoImpl userDao;
    @InjectMocks
    private UserServiceImpl userService;
    private List<User> users;
    @BeforeAll
    void setUp(){
        userDao = Mockito.mock(UserDaoImpl.class);
        users = List.of(new User(), User.builder().id(2).build());
    }

    @Test
    void getById() throws SQLException {
        User user = User.builder().id(2).name("name").lastName("last").build();
        Mockito.when(userDao.getById(1)).thenReturn(user);

        assertThat(userService.getById(1)).isEqualTo(user);
    }

    @Test
    void getAll() throws SQLException {
        Mockito.when(userDao.getAll()).thenReturn(users);

        assertThat(userService.getAll()).isEqualTo(users);
    }

    @Test
    void getAllUsersFromBank() throws SQLException {
        Mockito.when(userDao.getAllUsersFromBank(1)).thenReturn(users);

        assertThat(userService.getAllUsersFromBank(1)).isEqualTo(users);
    }

    @Test
    void addUser() throws SQLException {
        Mockito.when(userDao.addUser(new User())).thenReturn(100);
        int id = userService.addUser(new User());

        assertThat(id).isEqualTo(100);
    }

    @Test
    void addUserInBank() throws SQLException {
        userService.addUserInBank(User.builder()
                .id(111)
                .name("name")
                .secondName("secondname")
                .lastName("lastname")
                .phoneNumber("")
                .build(), 1);


        Mockito.verify(userDao).addUserInBank(User.builder()
                .id(111)
                .name("name")
                .secondName("secondname")
                .lastName("lastname")
                .phoneNumber("")
                .build(), 1);

    }

    @Test
    void updateUser() throws SQLException {
        userService.updateUser(new User());
        Mockito.verify(userDao).updateUser(new User());
    }

    @Test
    void deleteUserById() throws SQLException {
        userService.deleteUserById(100);
        Mockito.verify(userDao).deleteUserById(100);
    }
}