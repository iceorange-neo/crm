package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

/**
 * @author neo
 * @date 2021/3/3
 * @time 16:37
 */
public interface ClueActivityRelationDao {
    int unlocate(String id);

    int association(ClueActivityRelation car);

    List<ClueActivityRelation> getListByClueId(String clueId);

    int delete(ClueActivityRelation clueActivityRelation);
}
