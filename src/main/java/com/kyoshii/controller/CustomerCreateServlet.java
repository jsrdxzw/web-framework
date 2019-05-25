package com.kyoshii.controller;

import com.kyoshii.model.Customer;
import com.kyoshii.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author xuzhiwei
 */
@WebServlet(name = "CustomerServlet", urlPatterns = "/customer")
public class CustomerCreateServlet extends HttpServlet {

    private CustomerService customerService;

    @Override
    public void init() throws ServletException {
        super.init();
        customerService = new CustomerService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Customer> customerList = customerService.getCustomerList();
        request.setAttribute("customerList",customerList);
        request.getRequestDispatcher("/WEB-INF/view/customer.jsp").forward(request,response);
    }
}
