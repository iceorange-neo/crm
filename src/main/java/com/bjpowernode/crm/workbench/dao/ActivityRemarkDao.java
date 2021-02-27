package com.bjpowernode.crm.workbench.dao;

/**
 * @author neo
 * @date 2021/2/20
 * @time 20:32
 */
public interface ActivityRemarkDao {
    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    int delete(String[] ids);
}
