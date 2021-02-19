package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.IUserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.IUserService;
import com.bjpowernode.crm.utils.SqlSessionUtil;

/**
 * @author neo
 * @date 2021/2/18
 * @time 21:51
 */
public class IUserServiceImpl implements IUserService {
    private IUserDao userDao = SqlSessionUtil.getSqlSession().getMapper(IUserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) {



        return null;
    }
}
