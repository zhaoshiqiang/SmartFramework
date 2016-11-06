package org.smart4j.framework.test.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.test.util.CollectionUtil;
import org.smart4j.framework.test.util.PropsUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zhaoshiqiang on 2016/11/4.
 */
public final class DatabaseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);
    private static final QueryRunner QUERY_RUNNER;
    //��֤һ���߳�ֻ��һ��connection������ǰ�س��е�Connection����ThreadLocal�д����������Խ�ThreadLocal���Ϊһ�������̵߳�����
    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    //�����ݿ����ӡ��ػ���������Ƶ���������ٴ�����������ʧ����DBCP
    private static final BasicDataSource DATA_SOURCE;

    static {

        CONNECTION_HOLDER = new ThreadLocal<Connection>();
        QUERY_RUNNER = new QueryRunner();
        //��ӦMavenĿ¼�ṹ���ԣ�classpathָ����java��resources��������Ŀ¼
        String driver = ConfigHelper.getJdbcDriver();
        String url = ConfigHelper.getJdbcUrl();
        String username = ConfigHelper.getJdbcUsername();
        String password = ConfigHelper.getJdbcPassword();

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);
    }

    public static Connection getConnection(){

        Connection connection = CONNECTION_HOLDER.get();
        if (connection == null){
            try {
                connection = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
        return connection;
    }

    /**
     * ִ�в�ѯ���
     */
    public List<Map<String, Object>> executeQuery(String sql, Object... params) {
        //map��ʶ��������ֵ��ӳ���ϵ
        List<Map<String, Object>> result;
        try {
            Connection conn = getConnection();
            result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (Exception e) {
            LOGGER.error("execute query failure", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * ִ�и�����䣨������update��insert��delete��
     */
    public static int executeUpdate(String sql, Object... params) {
        int rows = 0;
        try {
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            LOGGER.error("execute update failure", e);
            throw new RuntimeException(e);
        }
        return rows;
    }

    public static <T> List<T> queryEntityList(Class<T> entityClass,String sql, Object... params){
//        List<T> entityList;
//        try {
//            //DbUtils�ṩ��QueryRunner�����������ʵ��(Entity)���в�ѯ��ʵ���ϣ�DbUtils����ִ��SQL���
//            //������һ��ResultSet�����ͨ������ȥ��������ʼ��ʵ����
//            entityList = QUERY_RUNNER.query(conn,sql,new BeanListHandler<T>(entityClass),params);
//        }catch (SQLException e){
//            LOGGER.error("query entity list failure",e);
//            throw new RuntimeException(e);
//        }
//        return entityList;

        List<T> entityList;
        try {
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn,sql,new BeanListHandler<T>(entityClass),params);
        }catch (SQLException e){
            LOGGER.error("query entity list failure",e);
            throw new RuntimeException(e);
        }
        return entityList;
    }

    /**
     * ��ѯʵ��
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity;
        try {
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity failure", e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    /**
     * ����ʵ��
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap){
        if (CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not insert entity: fieldMap is empty");
            return false;
        }

        String sql = "INSERT INTO " + getTableName(entityClass);
        StringBuffer columns = new StringBuffer("(");
        StringBuffer values = new StringBuffer("(");
        for (String fieldName : fieldMap.keySet()){
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql += columns + " VALUES " + values;
        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql,params) == 1;

    }

    /**
     * ����ʵ��
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can not update entity: fieldMap is empty");
            return false;
        }

        String sql = "UPDATE " + getTableName(entityClass) + " SET ";
        StringBuilder columns = new StringBuilder();
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(" = ?, ");
        }
        sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE id = ?";

        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();

        return executeUpdate(sql, params) == 1;
    }

    /**
     * ɾ��ʵ��
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
        String sql = "DELETE FROM " + getTableName(entityClass) + " WHERE id = ?";
        return executeUpdate(sql, id) == 1;
    }

    /**
     *ִ��SQL�ļ�
     */
    public static void executeSqlFile(String filePath){
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String sql;
            while ((sql = reader.readLine()) != null){
                executeUpdate(sql);
            }
        }catch (Exception e){
            LOGGER.error("execute sql file failure", e);
            throw new RuntimeException(e);
        }
    }

    private static String getTableName(Class<?> entityClass){
        return entityClass.getSimpleName();
    }
}
