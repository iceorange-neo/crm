package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

import java.util.List;

/**
 * @author neo
 * @date 2021/3/3
 * @time 16:37
 */
public interface ClueRemarkDao {
    List<ClueRemark> getListByClueId(String clueId);

    int delete(ClueRemark clueRemark);
}
