package com.bjpowernode.crm.workbench.web.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author neo
 * @date 2021/2/20
 * @time 18:26
 */
public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入市场活动控制器");

        String path = request.getServletPath();
        if("/workbench/activity/xxx.do".equals(path)){

            // xxx(request, response);

        }else if("/workbench/activity/xxx.do".equals(path)){

            // xxx(request, response);

        }


    }
}
