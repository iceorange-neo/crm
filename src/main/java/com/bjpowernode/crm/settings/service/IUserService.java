package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.User;

/**
 * @author neo
 * @date 2021/2/18
 * @time 21:50
 */
public interface IUserService {
    User login(String loginAct, String loginPwd, String ip);
}


