package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Clue;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author neo
 * @date 2021/3/3
 * @time 16:37
 */
public interface ClueDao {
    int save(Clue clue);

    List<Clue> getClueListByCondition(Map<String, Object> map   );

    int getTotalByCondition(Map<String, Object> map);

    Clue detail(String id);

    Clue getClueById(String id);
}
