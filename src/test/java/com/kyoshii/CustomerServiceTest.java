package com.kyoshii;

import com.kyoshii.helper.DatabaseHelper;
import com.kyoshii.model.Customer;
import com.kyoshii.service.CustomerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerServiceTest {
    private final CustomerService customerService;

    public CustomerServiceTest() {
        this.customerService = new CustomerService();
    }

    @Before
    public void init(){
        String file = "sql/customer_init.sql";
        DatabaseHelper.executeSqlFile(file);
    }

    @Test
    public void getCustomerListTest() {
        List<Customer> customers = DatabaseHelper.queryEntityList(Customer.class, "select * from customer");
        for (Customer customer : customers) {
            System.out.println(customer);
        }
//        List<Customer> customerList = customerService.getCustomerList();
//        Assert.assertEquals(2, customerList.size());
    }

    @Test
    public void getCustomerByIdTest() {
        Customer customer = DatabaseHelper.queryEntity(Customer.class, "select * from customer where id=?", 3);
        System.out.println(customer);
    }

    @Test
    public void createCustomerTest() {
        HashMap<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("name", "customer100");
        fieldMap.put("contact", "John");
        fieldMap.put("telephone", "13921697198");
        DatabaseHelper.insertEntity(Customer.class,fieldMap);
//        boolean result = customerService.createCustomer(fieldMap);
//        Assert.assertTrue(result);
    }

    @Test
    public void updateCustomerTest() {
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("email", "Eric@gmail.com");
        DatabaseHelper.updateEntity(Customer.class,3,fieldMap);
//        boolean result = customerService.updateCustomer(1, fieldMap);
//        Assert.assertTrue(result);
    }

    @Test
    public void deleteCustomerTest() {
        DatabaseHelper.executeDelete(Customer.class,3);
//        boolean result = customerService.deleteCustomer(1);
//        Assert.assertTrue(result);
    }
}
