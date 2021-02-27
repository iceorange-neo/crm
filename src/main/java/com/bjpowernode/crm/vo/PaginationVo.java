package com.bjpowernode.crm.vo;

import java.util.List;

/**
 * @author neo
 * @date 2021/2/24
 * @time 21:03
 */
public class PaginationVo<T> {

    private int total;
    private List<T> dataList;

    public PaginationVo() {
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
