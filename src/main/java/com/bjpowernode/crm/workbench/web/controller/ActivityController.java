package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.IUserService;
import com.bjpowernode.crm.settings.service.impl.IUserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import jdk.nashorn.internal.ir.Flags;

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
 * @date 2021/2/20
 * @time 18:26
 */
public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入市场活动控制器");

        String path = request.getServletPath();
        System.out.println("path========================" + path);
        if("/workbench/activity/getUserList.do".equals(path)){

             getUserList(request, response);

        }else if("/workbench/activity/save.do".equals(path)){

             save(request, response);

        }else if("/workbench/activity/pageList.do".equals(path)){

            pageList(request, response);
            
        }else if("/workbench/activity/delete.do".equals(path)){

            delete(request, response);

        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)){

            getUserListAndActivity(request, response);

        }else if("/workbench/activity/update.do".equals(path)){

            update(request, response);

        }else if("/workbench/activity/detail.do".equals(path)){

            detail(request, response);
        }else if("/workbench/activity/getRemarkListByAid.do".equals(path)){

            getRemarkListByAid(request, response);

        }else if("/workbench/activity/deleteRemark.do".equals(path)){

            deleteRemark(request, response);

        }else if("/workbench/activity/saveRemark.do".equals(path)){

            saveRemark(request, response);

        }else if("/workbench/activity/updateRemark.do".equals(path)){

            updateRemark(request, response);

        }


    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入市场备注信息修改操作");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";  // 1表示已经修改

        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setEditFlag(editFlag);
        activityRemark.setEditTime(editTime);
        activityRemark.setEditBy(editBy);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.updateRemark(activityRemark);

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("activityRemark", activityRemark);

        PrintJson.printJsonObj(response, map);



    }


    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入备注信息添加");

        String noteContent = request.getParameter("noteContent");
        String activityId = request.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "0";
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setActivityId(activityId);
        activityRemark.setCreateBy(createBy);
        activityRemark.setCreateTime(createTime);
        activityRemark.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.saveRemark(activityRemark);

        // 后端需要向前端返回的数据有success、activityRemark
        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("activityRemark", activityRemark);

        PrintJson.printJsonObj(response, map);



    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入备注信息删除======================");

        String id = request.getParameter("id");

        System.out.println(id);
        System.out.println("===================================");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.deleteRemark(id);

        System.out.println("=================" + flag);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据市场活动的id取得备注信息列表");

        String activityId = request.getParameter("activityId");
        System.out.println("activityId=========================" + activityId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<ActivityRemark> remarks = as.getRemarkListByAid(activityId);

        PrintJson.printJsonObj(response, remarks);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入跳转到详细信息页的操作");

        String id = request.getParameter("id");

        System.out.println("=====================id==========:" + id);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity activity = as.detail(id);

        request.setAttribute("activity", activity);

        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);


    }

    private void update(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入执行市场活动修改操作");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");

        // 修改时间
        String editTime = DateTimeUtil.getSysTime();
        // 修改人:当前登录的用户
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setName(name);
        activity.setOwner(owner);
        activity.setCost(cost);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setDescription(description);
        activity.setEditBy(editBy);
        activity.setEditTime(editTime);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.update(activity);

    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行获得用户列表和根据id获取单条市场活动信息对象的controller");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*
            分析：
                前端页面需要获取一个userList
                还需要获得一个activity对象
            考虑到复用率不大，故采取map封装的形式将其返回
         */
        Map<String, Object> map = as.getUserListAndActivity(id);

        PrintJson.printJsonObj(response, map);

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动的删除操作");

        String[] ids = request.getParameterValues("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.delete(ids);

        PrintJson.printJsonFlag(response, flag);


    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询市场活动信息列表的操作（结合条件查询以及分页查询）");
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        int pageNo = Integer.valueOf(request.getParameter("pageNo"));
        // 每页展现的记录数
        int pageSize = Integer.valueOf(request.getParameter("pageSize"));
        // 计算略过的记录数
        int skipCount = (pageNo - 1) * pageSize;

        // 注意：需要将数据封装了之后传递给service层
        // 但是：Vo是从后台查出数据将其封装给前端传递
        // 遇到此种情况可以使用map
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*

            前端需要的是市场活动信息列表和
            一个查询的总记录条数
            业务层拿到了以上两项信息之后，然后怎么返回数据呢
            map
            map.put("total":total)
            map.put("dataList":dataList);
            map-----json

            --------------
            vo   *
            （复用率少就临时使用map、复用率高就是用Vo）

            paginationVo<T>
                private int total;
                private List<T> dataList;

            PaginationVo<Activity> vo = new PaginationVo<>();
            vo.setTotal(total),
            vo.setDataList(dataList),
            将来分页查询每个模块都有，所以我们选择使用通用的Vo操作起来比较方便
            PrintJson  vo ---- json字符串

         */
        PaginationVo<Activity> vo = as.pageList(map);

        // vo ---- json字符串
        PrintJson.printJsonObj(response, vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");

        // 创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        // 创建人，当前登录的用户    从session作用域对象中取出。
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateBy(createBy);
        activity.setCreateTime(createTime);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.save(activity);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得用户信息列表");

        IUserService us = (IUserService) ServiceFactory.getService(new IUserServiceImpl());

        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response, uList);
    }
}
