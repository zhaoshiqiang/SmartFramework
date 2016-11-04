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
        Connection conn = null;
        try {
            List<Customer> customerList = new ArrayList<Customer>();
            String sql = "select * from customer";
            conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Customer customer = new Customer();
                customer.setId(rs.getLong("id"));
                customer.setName(rs.getString("name"));
                customer.setContact(rs.getString("contact"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setEmail(rs.getString("email"));
                customer.setEmail(rs.getString("email"));
                customer.setRemark(rs.getString("remark"));
                customerList.add(customer);
            }
            return customerList;
        }catch (SQLException e){
            LOGGER.error("execute sql failure",e);
        }finally {
            DatabaseHelper.closeConnection(conn);
        }
        return null;
    }
    //获取客户
    public Customer getCustomer(Long id){
        return null;
    }
    //创建客户
    public boolean createCustomer(Map<String,Object> fieldMap){
        return false;
    }
    //更新客户
    public boolean updateCustomer(long id,Map<String,Object> fieldMap){
        return false;
    }
    //删除客户
    public boolean deleteCustomer(long id){
        return false;
    }

}
