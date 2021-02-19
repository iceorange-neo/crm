package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.IUserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.IUserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author neo
 * @date 2021/2/18
 * @time 21:51
 */
public class IUserServiceImpl implements IUserService {
    private IUserDao userDao = SqlSessionUtil.getSqlSession().getMapper(IUserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException{

        Map<String, String> map = new HashMap<String, String>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        User user = userDao.login(map);
        System.out.println("==========================User对象是" + user);
        if(user == null){

            throw new LoginException("账号密码错误");
        }

        // 如果程序成功执行到该行，说明账号、密码正确
        // 需要继续向下验证其他三项信息

        // 验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(currentTime) < 0){

            throw new LoginException("账号已失效");

        }

        // 验证锁定状态
        String lockState = user.getLockState();
        if("0".equals(lockState)){

            throw new LoginException("账号已锁定");
        }

        // 判断ip地址
        String allowIps = user.getAllowIps();
        if(!allowIps.contains(ip)){

            throw new LoginException("ip地址受限，请联系管理员");

        }
        return user;
    }
}
