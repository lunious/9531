package com.lubanjianye.biaoxuntong.bean;

import java.io.Serializable;



public class IndexListBean implements Serializable {

    private int id = 0;
    private String entryName = null;
    private String type = null;
    private String entity = null;
    private int entityId = 0;
    private String sysTime = null;
    private String deadTime = null;
    private String signstauts = null;
    private String address = null;

    private String isResult = null;
    private String isCorrections = null;
    private String entityUrl = null;

    public String getEntityUrl() {
        return entityUrl;
    }

    public void setEntityUrl(String entityUrl) {
        this.entityUrl = entityUrl;
    }

    public String getIsResult() {
        return isResult;
    }

    public void setIsResult(String isResult) {
        this.isResult = isResult;
    }

    public String getIsCorrections() {
        return isCorrections;
    }

    public void setIsCorrections(String isCorrections) {
        this.isCorrections = isCorrections;
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

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public String getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(String deadTime) {
        this.deadTime = deadTime;
    }

    public String getSignstauts() {
        return signstauts;
    }

    public void setSignstauts(String signstauts) {
        this.signstauts = signstauts;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "IndexListBean{" +
                "id=" + id +
                ", entryName='" + entryName + '\'' +
                ", type='" + type + '\'' +
                ", entity='" + entity + '\'' +
                ", entityId=" + entityId +
                ", sysTime='" + sysTime + '\'' +
                ", deadTime='" + deadTime + '\'' +
                ", signstauts='" + signstauts + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
