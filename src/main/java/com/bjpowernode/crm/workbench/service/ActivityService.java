package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.Map;

/**
 * @author neo
 * @date 2021/2/20
 * @time 18:24
 */
public interface ActivityService {

    public boolean save(Activity activity);

    PaginationVo<Activity> pageList(Map<String, Object> map);
}
