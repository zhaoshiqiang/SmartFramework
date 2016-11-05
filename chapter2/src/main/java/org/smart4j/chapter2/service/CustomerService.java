package org.smart4j.chapter2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.helper.DatabaseHelper;
import org.smart4j.chapter2.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 提供客户数据服务
 * Created by zhaoshiqiang on 2016/11/4.
 */
public class CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    //获取客户列表
    public List<Customer> getCustomerList(String keyword){
//        Connection conn = null;
//        List<Customer> customerList = new ArrayList<Customer>();
//        try {
//            String sql = "select * from customer";
//            conn = DatabaseHelper.getConnection();
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()){
//                Customer customer = new Customer();
//                customer.setId(rs.getLong("id"));
//                customer.setName(rs.getString("name"));
//                customer.setContact(rs.getString("contact"));
//                customer.setTelephone(rs.getString("telephone"));
//                customer.setEmail(rs.getString("email"));
//                customer.setEmail(rs.getString("email"));
//                customer.setRemark(rs.getString("remark"));
//                customerList.add(customer);
//            }
//            return customerList;
//        }catch (SQLException e){
//            LOGGER.error("execute sql failure",e);
//        }finally {
//            DatabaseHelper.closeConnection(conn);
//        }
//        return customerList;

        /*
        * 使用DbUtils类库后不需要再面对PreparedStatement和ResultSet了，只需要使用DatabaseHelper就能执行数据库操作。
        * 接下来就是让Connection对开发人员完全透明，也就是说，如何隐藏掉创建与关闭Connection代码？
        * */
//        Connection conn = DatabaseHelper.getConnection();
//        try {
//            String sql = "select * from customer";
//            return DatabaseHelper.queryEntityList(Customer.class,conn,sql,null);
//        }finally {
//            DatabaseHelper.closeConnection(conn);
//        }

        String sql = "select * from customer";
        return DatabaseHelper.queryEntityList(Customer.class,sql,null);

    }
    /**
     * 获取客户
     */
    public Customer getCustomer(long id) {
        String sql = "SELECT * FROM customer WHERE id = ?";
        return DatabaseHelper.queryEntity(Customer.class, sql, id);
    }

    /**
     * 创建客户
     */
    public boolean createCustomer(Map<String, Object> fieldMap) {
        return DatabaseHelper.insertEntity(Customer.class, fieldMap);
    }

    /**
     * 更新客户
     */
    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    /**
     * 删除客户
     */
    public boolean deleteCustomer(long id) {
        return DatabaseHelper.deleteEntity(Customer.class, id);
    }

}
