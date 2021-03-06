package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

/**
 * @author neo
 * @date 2021/2/20
 * @time 18:21
 */
public interface ActivityDao {

    int save(Activity activity);

    int getTotalByCondition(Map<String, Object> map);

    List<Activity> getActivityListByCondition(Map<String, Object> map);

    Activity getById(String id);

    int update(Activity activity);

    Activity detail(String id);

    List<Activity> showActivityListByClueId(String clueId);

    List<Activity> getActivityListByName(Map<String, String> map);

    List<Activity> getActivityListByNameLike(String activityName);
}

