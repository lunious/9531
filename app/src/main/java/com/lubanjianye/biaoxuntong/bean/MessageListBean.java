package com.lubanjianye.biaoxuntong.bean;

import java.io.Serializable;

/**
 * 项目名:   Lunious
 * 包名:     com.lubanjianye.biaoxuntong.bean
 * 文件名:   ResultListBean
 * 创建者:   lunious
 * 创建时间: 2017/11/29  22:37
 * 描述:     TODO
 */

public class MessageListBean implements Serializable {

    private int id = 0;
    private String entityName = null;
    private String entityType = null;
    private String entity = null;
    private int entityId = 0;
    private String createTime = null;
    private String isRead = null;

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ResultListBean{" +
                "id=" + id +
                ", entityName='" + entityName + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entity='" + entity + '\'' +
                ", entityId=" + entityId +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
