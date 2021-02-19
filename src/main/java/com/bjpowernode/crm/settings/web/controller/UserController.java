package com.bjpowernode.crm.settings.web.controller;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author neo
 * @date 2021/2/18
 * @time 21:54
 */
public class UserController extends HttpServlet {
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入用户控制器");
        // 获取的是url-pattern部分
        String path = request.getServletPath();

        if("/settings/user/xxx.do".equals(path)){

            // xxx(request, response);

        }else if("/settings/user/xxx.do".equals(path)){

            // xxx(request, response);
        }

    }
}
