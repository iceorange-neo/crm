package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author neo
 * @date 2021/2/20
 * @time 14:37
 */
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        System.out.println("进入到验证有没有登录过的过滤器");

        HttpServletRequest request =  (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // 不应该被拦截的资源自动放行
        String path = request.getServletPath();
        if("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){

            chain.doFilter(req, resp);

        // 其他资源必须验证有没有登录过
        }else{


            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            // 如果user不为null，说明登录过
            if(user != null){
                // 放行
                chain.doFilter(req, resp);

                // 没有登录过
            }else{

                // 重定向到登录页
            /*

                重定向的路径怎么写？
                    在实际项目开发中，对于路径的使用，无论前端还是后端，应该一律使用绝对路径
                    关于转发和重定向的路径的写法如下：
                        转发：
                            使用的是一种特殊的绝对路径的使用方式，这种路径前面不加/项目名，这种路径也称为之内部路径
                            /login.jsp
                        重定向：
                           使用的是传统绝对路径的写法，前面必须以/项目名开头，后面跟具体的资源路径
                           /crm/login.jsp

                为什么使用重定向，使用转发不行吗？
                    转发之后路径会停留在老路径上，而不是跳转之后最新资源的路径
                    我们应该在为用户跳转到登录页的同时，将浏览器的地址栏自动设置为当前登录页面的路径。
             */
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }

        }
    }
}
