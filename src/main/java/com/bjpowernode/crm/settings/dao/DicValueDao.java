package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.DicValue;

import java.util.List;

/**
 * @author neo
 * @date 2021/3/3
 * @time 19:36
 */
public interface DicValueDao {
    List<DicValue> getDicValueList(String code);
}
