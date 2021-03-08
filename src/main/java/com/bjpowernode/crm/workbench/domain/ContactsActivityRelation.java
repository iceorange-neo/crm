package com.bjpowernode.crm.workbench.domain;

/**
 * @author neo
 * @date 2021/3/3
 * @time 16:33
 */
public class ContactsActivityRelation {
    private String id;
    private String contactsId;
    private String activityId;

    public ContactsActivityRelation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactsId() {
        return contactsId;
    }

    public void setContactsId(String contactsId) {
        this.contactsId = contactsId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
