package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.IUserService;
import com.bjpowernode.crm.settings.service.impl.IUserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author neo
 * @date 2021/3/3
 * @time 19:17
 */
public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到线索控制器");

        // request.getServletPath得到的字符串是url-pattern
        String path = request.getServletPath();

        if("/workbench/clue/getUserList.do".equals(path)){

            getUserList(request, response);

        }else if("/workbench/clue/save.do".equals(path)){

            save(request, response);

        }else if("/workbench/clue/cluePageList.do".equals(path)){

            cluePageList(request, response);

        }else if("/workbench/clue/detail.do".equals(path)){
            
            detail(request, response);
        }
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到线索的详细信息页");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue clue = cs.detail(id);

        request.setAttribute("clue",clue);

        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);

    }

    private void cluePageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入线索列表展示控制器");

        int pageNo = Integer.valueOf(request.getParameter("pageNo"));
        int pageSize = Integer.valueOf(request.getParameter("pageSize"));

        // 略过的记录数
        int skipCount = (pageNo - 1) * pageSize;

        String fullname = request.getParameter("fullname");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");

        /*
            将数据封装为一个Clue对象传递给service层

         */
        Map<String, Object> map = new HashMap<>();
        map.put("fullname", fullname);
        map.put("owner", owner);
        map.put("company", company);
        map.put("phone", phone);
        map.put("mphone", mphone);
        map.put("state", state);
        map.put("source", source);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        /*
            后端向前端返回一个VO，因为分页查询在这个模块中都有，故复用率高
            private int total;
            private List<T> clueList;
         */
        PaginationVo<Clue> vo = cs.cluePageList(map);

        PrintJson.printJsonObj(response, vo);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入线索模块的线索信息保存控制器");
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime  = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        String id = UUIDUtil.getUUID();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();

        Clue clue = new Clue();
        clue.setId(id);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setAddress(address);
        clue.setNextContactTime(nextContactTime);
        clue.setContactSummary(contactSummary);
        clue.setDescription(description);
        clue.setSource(source);
        clue.setState(state);
        clue.setMphone(mphone);
        clue.setWebsite(website);
        clue.setPhone(phone);
        clue.setEmail(email);
        clue.setFullname(fullname);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setOwner(owner);
        clue.setAppellation(appellation);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.save(clue);

        PrintJson.printJsonFlag(response, flag);


    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入线索模块的获取用户信息列表控制器");
        IUserService us = (IUserService) ServiceFactory.getService(new IUserServiceImpl());
        List<User> userList = us.getUserList();
        PrintJson.printJsonObj(response, userList);

    }
}