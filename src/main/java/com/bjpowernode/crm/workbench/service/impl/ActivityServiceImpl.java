package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

/**
 * @author neo
 * @date 2021/2/20
 * @time 18:24
 */
public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    @Override
    public boolean save(Activity activity) {

        boolean  flag = true;

        int count = activityDao.save(activity);
        if(count != 1){

            flag = false;

        }
        return flag;
    }

    @Override
    public PaginationVo<Activity> pageList(Map<String, Object> map) {

        // 取得total
        int total = activityDao.getTotalByCondition(map);

        // 取得dataList
        List<Activity> dataList =  activityDao.getActivityListByCondition(map);

        // 创建一个vo对象，将total和dataList封装到Vo中
        PaginationVo<Activity> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        // 将vo返回
        return vo;

    }





















}
