package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Customer;

/**
 * @author neo
 * @date 2021/3/3
 * @time 16:39
 */
public interface CustomerDao {
    Customer getCustomerByName(String company);

    int save(Customer customer);
}
