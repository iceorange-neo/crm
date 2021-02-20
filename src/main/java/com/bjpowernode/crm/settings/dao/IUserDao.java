package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @author neo
 * @date 2021/2/18
 * @time 21:48
 */
public interface IUserDao {

    User login(Map<String, String> map);

    List<User> getUserList();
}
