package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.DicType;

import java.util.List;

/**
 * @author neo
 * @date 2021/3/3
 * @time 19:35
 */
public interface DicTypeDao {
    List<DicType> getDicTypeCodeList();
}
