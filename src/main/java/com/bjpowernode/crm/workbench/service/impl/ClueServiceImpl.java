package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.dao.ClueActivityRelationDao;
import com.bjpowernode.crm.workbench.dao.ClueDao;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

/**
 * @author neo
 * @date 2021/3/3
 * @time 19:16
 */
public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);


    @Override
    public boolean save(Clue clue) {

        boolean flag = true;

        int count = clueDao.save(clue);
        if (count != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVo<Clue> cluePageList(Map<String, Object> map) {

        // 取得total
        int total = clueDao.getTotalByCondition(map);

        // 取得clueList
        List<Clue> clueList = clueDao.getClueListByCondition(map);

        // 创建vo，将total和clueList封装到vo中

        PaginationVo<Clue> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(clueList);
        return vo;
    }

    @Override
    public Clue detail(String id) {

        Clue clue = clueDao.detail(id);
        return clue;
    }

    @Override
    public boolean unlocate(String id) {
        boolean flag = true;

        // 设计到tbl_clue_activity_relation
        int count = clueActivityRelationDao.unlocate(id);

        if(count != 1){
            flag = false;
        }

        return flag;
    }
}
