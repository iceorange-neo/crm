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
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
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

        }else if("/workbench/clue/showActivityList.do".equals(path)){

            showActivityList(request, response);

        }else if("/workbench/clue/unlocate.do".equals(path)){

            unlocate(request, response);

        }else if("/workbench/clue/getUserListAndClue.do".equals(path)){
            
            getUserListAndClue(request, response);

        }else if("/workbench/clue/getActivityListByName.do".equals(path)){

            getActivityListByName(request, response);

        }else if("/workbench/clue/association.do".equals(path)){

            association(request, response);

        }else if("/workbench/clue/getActivityListByNameLike.do".equals(path)){

            getActivityListByNameLike(request, response);

        }else if("/workbench/clue/convert.do".equals(path)){

            convert(request, response);

        }else if("/workbench/clue/delete.do".equals(path)){

            delete(request, response);

        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入线索信息删除");

        // 接收所需要删除的参数信息
        String[] ids = request.getParameterValues("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.delete(ids);

        PrintJson.printJsonFlag(response, flag);
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("进入线索转换：是否创建交易和不创建交易");

        String clueId = request.getParameter("clueId");
        // 接收是否需要创建交易的标记
        String flag = request.getParameter("flag");

        Tran t = null;

        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        // 如果需要创建交易
        if("a".equals(flag)){
            t = new Tran();

            // 接收交易表单中的所有参数
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();

            t.setId(id);
            t.setMoney(money);
            t.setActivityId(activityId);
            t.setCreateTime(createTime);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setCreateBy(createBy);

        }

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        /*
            为业务层传递的参数：
            1、必须传递的参数clueId，有了这个clueId才知道转换的是哪一条参数
            2、必须传递的参数Tran，因为在线索转换的过程中，有可能会创建一笔交易（业务层接收到的Tran有可能是null）
            3、当将来我们需要在业务层做大量的添加操作的时候：因为创建时间、id等都可以通过工具类生成(可以在业务层取得)，但是我们的创建人不行，只能从request域中取得
                但是，若我们将request对象传递给业务层就不合理，我们基于MVC标准分层开发思想。
                故此我们直接传递createBy。

         */
        boolean success = cs.convert(clueId, t, createBy);

        // 对于传统请求怎么响应：请求转发或者重定向（此处采用重定向）
        // 因为没有使用到request域
        if(success){

            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");

        }

    }

    private void getActivityListByNameLike(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据市场活动名称模糊查询市场活动列表");

        String activityName = request.getParameter("activityName");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByNameLike(activityName);

        PrintJson.printJsonObj(response, aList);
    }

    private void association(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入线索、市场活动关联");

        String clueId = request.getParameter("clueId");
        String[] activityIds = request.getParameterValues("activityId");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.association(clueId, activityIds);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入通过name模糊查询activityList市场活动列表，排除掉clueId已经关联了的");

        String activityName = request.getParameter("activityName");
        String clueId = request.getParameter("clueId");

        Map<String, String> map = new HashMap<>();
        map.put("activityName", activityName);
        map.put("clueId", clueId);

        // 市场活动相关的操作
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());


        List<Activity> aList = as.getActivityListByName(map);

        PrintJson.printJsonObj(response, aList);
    }

    private void getUserListAndClue(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入线索模块的线索记录铺设");
        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Map<String, Object> map = cs.getUserListAndClue(id);

        PrintJson.printJsonObj(response, map);

    }

    private void unlocate(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入解除关联相关操作");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.unlocate(id);

        PrintJson.printJsonFlag(response, flag);

    }

    private void showActivityList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入线索模块的展现市场活动列表");

        String clueId = request.getParameter("clueId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.showActivityListByClueId(clueId);

        PrintJson.printJsonObj(response, aList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到线索的详细信息页");

        String id = request.getParameter("id");
        System.out.println("线索id====" + id);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue clue = cs.detail(id);
        System.out.println(clue);

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
