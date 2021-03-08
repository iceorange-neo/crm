package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.Map;

/**
 * @author neo
 * @date 2021/3/3
 * @time 19:16
 */
public interface ClueService {

    boolean save(Clue clue);

    PaginationVo<Clue> cluePageList(Map<String, Object> map);

    Clue detail(String id);

    boolean unlocate(String id);

    Map<String, Object> getUserListAndClue(String id);

    boolean association(String clueId, String[] activityIds);
}
