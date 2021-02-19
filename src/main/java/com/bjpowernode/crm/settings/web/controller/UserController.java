package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.IUserService;
import com.bjpowernode.crm.settings.service.impl.IUserServiceImpl;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

        if("/settings/user/login.do".equals(path)){

            login(request, response);

        }else if("/settings/user/xxx.do".equals(path)){

            // xxx(request, response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response){
        System.out.println("进入到登录操作");

        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        // 将密码的明文形式转化为MD5
        loginPwd = MD5Util.getMD5(loginPwd);
        //
        System.out.println("----------------------经过MD5加密的密码是：" + loginPwd);
        // 接收客户端ip地址
        String ip = request.getRemoteAddr();
        System.out.println("-----------------------ip:" + ip);

        // 未来业务层开发，统一使用代理类形态的接口对象
        IUserService userService = (IUserService) ServiceFactory.getService(new IUserServiceImpl());
        try{

            User user = userService.login(loginAct, loginPwd, ip);

            request.getSession().setAttribute("user", user);

            // 如果程序执行到此处，说明业务层没有为controller抛出任何的异常
            // 表示登录成功
            /*
                {"success":true}
             */
//            String str = "{\"success\":true}";
//            response.getWriter().print(str);
            // 使用工具类中的jackson工具
            PrintJson.printJsonFlag(response, true);    /* 该工具类其会自动根据相应对象写入 */

        }catch(Exception e){
            e.printStackTrace();

            // 一旦程序执行了catch块的信息，说明业务层为我们验证登录失败，为controller抛出了异常
            // 表示登录失败
            /*
                {"success":false,"msg":?}
             */
            String msg = e.getMessage();
            /*
                我们现在作为controller，需要为ajax请求提供多项信息
                可以有两种手段来处理：
                    1）将多项信息打包成为map，将map解析为json格式字符串
                    2）创建一个Vo
                        boolean success;
                        String msg;

                    如果对于展现的信息将来可以大量的使用，我们创建一个Vo类，使用方便
                    如果对于展现的信息只有在此需求中能够使用，我们使用map就可以了
             */
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(response, map);
        }




    }
}
