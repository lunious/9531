package com.lubanjianye.biaoxuntong.bean;

import java.io.Serializable;



public class SortColumnBean implements Serializable{
    private int id = 0;
    private String name = null;
    private boolean isShowDele = false;
    private boolean isChangeColo = false;
    private int isShow = 1;

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public boolean isShowDele() {
        return isShowDele;
    }

    public void setShowDele(boolean showDele) {
        isShowDele = showDele;
    }

    public boolean isChangeColo() {
        return isChangeColo;
    }

    public void setChangeColo(boolean changeColo) {
        isChangeColo = changeColo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SortColumnBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isShowDele=" + isShowDele +
                ", isChangeColo=" + isChangeColo +
                '}';
    }
}
