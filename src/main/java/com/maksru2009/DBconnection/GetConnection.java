package com.maksru2009.DBconnection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
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
        config = new HikariConfig("src/main/resources/db.properties");
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
