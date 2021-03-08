package com.bjpowernode.crm.workbench.domain;

/**
 * @author neo
 * @date 2021/3/3
 * @time 16:32
 */
public class ClueRemark {
    private String id;
    private String noteContent; // 内容
    private String createBy;    // 创建人
    private String createTime;  // 创建时间
    private String editBy;  // 修改人
    private String editTime;    // 修改时间
    private String editFlag;    // 标记位
    private String clueId;  // 外键，关联tbl_clue表

    public ClueRemark() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEditBy() {
        return editBy;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(String editFlag) {
        this.editFlag = editFlag;
    }

    public String getClueId() {
        return clueId;
    }

    public void setClueId(String clueId) {
        this.clueId = clueId;
    }
}
