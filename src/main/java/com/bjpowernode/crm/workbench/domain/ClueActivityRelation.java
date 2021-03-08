package com.bjpowernode.crm.workbench.domain;

/**
 * @author neo
 * @date 2021/3/7
 * @time 19:50
 */
public class ClueActivityRelation {
    private String id;  // 主键
    private String clueId;  // 外键，关联tbl_clue
    private String activityId;  // 外键， 关联tbl_activity

    public ClueActivityRelation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClueId() {
        return clueId;
    }

    public void setClueId(String clueId) {
        this.clueId = clueId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
