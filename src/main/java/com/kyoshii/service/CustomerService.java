package com.kyoshii.service;

import com.kyoshii.annotation.Service;
import com.kyoshii.annotation.Transaction;
import com.kyoshii.helper.DatabaseHelper;
import com.kyoshii.model.Customer;
import java.util.List;
import java.util.Map;

/**
 * @author xuzhiwei
 * 增加事务支持，一旦有失败则自动回滚
 */
@Service
public class CustomerService {


    public List<Customer> getCustomerList() {
        String sql = "select * from customer";
        return DatabaseHelper.queryEntityList(Customer.class, sql);
    }

    public Customer getCustomer(long id) {
        String sql = "select * from customer where id=?";
        return DatabaseHelper.queryEntity(Customer.class, sql, id);
    }

    @Transaction
    public boolean createCustomer(Map<String, Object> fieldMap) {
        return DatabaseHelper.insertEntity(Customer.class, fieldMap);
    }

    @Transaction
    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    @Transaction
    public boolean deleteCustomer(long id) {
        return DatabaseHelper.executeDelete(Customer.class, id);
    }
}
