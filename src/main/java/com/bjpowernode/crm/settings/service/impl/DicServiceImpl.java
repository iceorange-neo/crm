package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.DicTypeDao;
import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author neo
 * @date 2021/3/3
 * @time 19:38
 */
public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);


    @Override
    public Map<String, List<DicValue>> getAll() {

        Map<String, List<DicValue>> map = new HashMap<>();

        // 将字典类型列表取出
        List<DicType> dList = dicTypeDao.getDicTypeCodeList();

        // 将字典类型列表遍历
        for(int i = 0; i < dList.size(); i++){
            // 取得每一种类型的字典类型编码
            String code = dList.get(i).getCode();
            // 根据code值作为参数查询出来DicValue列表
            List<DicValue> dvList = dicValueDao.getDicValueList(code);

            map.put(code, dvList);
        }

        return map;
    }
}
