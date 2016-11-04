package org.smart4j.chapter2.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.util.PropsUtil;

import java.security.PublicKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by zhaoshiqiang on 2016/11/4.
 */
public class DatabaseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;
//    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
//
//    private static final QueryRunner QUERY_RUNNER;
//
//    private static final BasicDataSource DATA_SOURCE;

    static {

        Properties conf = PropsUtil.loadProps("config.properties");
        DRIVER = conf.getProperty("jdbc.driver");
        URL = conf.getProperty("jdbc.url");
        USERNAME = conf.getProperty("jdbc.username");
        PASSWORD = conf.getProperty("jdbc.password");

        try {
            Class.forName(DRIVER);
        }catch (ClassNotFoundException e){
            LOGGER.error("can not load jdbc driver",e);
        }

//        CONNECTION_HOLDER = new ThreadLocal<Connection>();
//        QUERY_RUNNER = new QueryRunner();
//        //对应Maven目录结构而言，classpath指的是java与resources这两个根目录
//        Properties conf = PropsUtil.loadProps("config.properties");
//        String driver = conf.getProperty("jdbc.driver");
//        String url = conf.getProperty("jdbc.url");
//        String username = conf.getProperty("jdbc.username");
//        String password = conf.getProperty("jdbc.password");
//
//        DATA_SOURCE = new BasicDataSource();
//        DATA_SOURCE.setDriverClassName(driver);
//        DATA_SOURCE.setUrl(url);
//        DATA_SOURCE.setUsername(username);
//        DATA_SOURCE.setPassword(password);
    }

    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e) {
            LOGGER.error("get connection failure",e);
        }
        return connection;
    }

    public static void closeConnection(Connection connection){
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("close connection failure",e);
            }
        }
    }
}
