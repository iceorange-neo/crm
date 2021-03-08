package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.IUserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.dao.ClueActivityRelationDao;
import com.bjpowernode.crm.workbench.dao.ClueDao;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.HashMap;
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
    private IUserDao userDao = SqlSessionUtil.getSqlSession().getMapper(IUserDao.class);


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

    @Override
    public Map<String, Object> getUserListAndClue(String id) {
        // 获取用户列表
        List<User> uList = userDao.getUserList();

        // 获取Clue线索信息(根据id查出)
        Clue clue = clueDao.getClueById(id);
        Map<String, Object> map = new HashMap<>();

        map.put("uList", uList);
        map.put("clue", clue);

        return map;
    }

    @Override
    public boolean association(String clueId, String[] activityIds) {

        boolean flag = true;
        for (String activityId : activityIds){

            // 给每一个activityId和clueId做关联
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(clueId);
            car.setActivityId(activityId);
            int count = clueActivityRelationDao.association(car);
            if(count != 1){
                flag = false;
            }

        }
        return flag;
    }
}
