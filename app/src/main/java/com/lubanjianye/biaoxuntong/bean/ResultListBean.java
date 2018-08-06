package com.lubanjianye.biaoxuntong.bean;

import java.io.Serializable;



public class ResultListBean implements Serializable {

    private int id = 0;
    private String entryName = null;
    private String type = null;
    private String entity = null;
    private int entityid = 0;
    private String sysTime = null;
    private String entityUrl = null;

    public String getEntityUrl() {
        return entityUrl;
    }

    public void setEntityUrl(String entityUrl) {
        this.entityUrl = entityUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public int getEntityid() {
        return entityid;
    }

    public void setEntityid(int entityid) {
        this.entityid = entityid;
    }

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    @Override
    public String toString() {
        return "ResultListBean{" +
                "id=" + id +
                ", entryName='" + entryName + '\'' +
                ", type='" + type + '\'' +
                ", entity='" + entity + '\'' +
                ", entityid=" + entityid +
                ", sysTime='" + sysTime + '\'' +
                '}';
    }
}
