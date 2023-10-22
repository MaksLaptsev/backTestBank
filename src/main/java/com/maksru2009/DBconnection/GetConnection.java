package com.maksru2009.DBconnection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Используется для получения соединения с бд
 * информация для конфигурации бд находится в src/main/resources
 * файл db.properties
 */
public class GetConnection {
    private static volatile Connection connection;
    private static HikariConfig config;
    private static HikariDataSource hds;

    private GetConnection(){
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("db.properties");
        Properties props = new Properties();
        try{
            props.load(resourceAsStream);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        config = new HikariConfig(props);
        hds = new HikariDataSource(config);
        try{
            connection = hds.getConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Проверка на наличие открытого соединения {@link Connection}, в случае его отсутствия - создание
     * @return - {@link Connection}
     */
    public static Connection getConnection(){
        if (connection == null) {
            synchronized (GetConnection.class) {
                if (connection == null) {
                    new GetConnection();
                }
            }
        }
        return connection;
    }
}
