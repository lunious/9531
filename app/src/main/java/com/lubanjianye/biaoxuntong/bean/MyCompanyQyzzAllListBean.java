package com.lubanjianye.biaoxuntong.bean;

import java.io.Serializable;

/**
 * 项目名:   Lunious
 * 包名:     com.lubanjianye.biaoxuntong.bean
 * 文件名:   MyCompanyQyzzAllListBean
 * 创建者:   lunious
 * 创建时间: 2017/11/30  11:36
 * 描述:     TODO
 */

public class MyCompanyQyzzAllListBean implements Serializable{

    private String lx_name = null;
    private String dl_name = null;
    private String xl_name = null;
    private String zy_name = null;
    private String dj = null;
    private String dq = null;

    public String getLx_name() {
        return lx_name;
    }

    public void setLx_name(String lx_name) {
        this.lx_name = lx_name;
    }

    public String getDl_name() {
        return dl_name;
    }

    public void setDl_name(String dl_name) {
        this.dl_name = dl_name;
    }

    public String getXl_name() {
        return xl_name;
    }

    public void setXl_name(String xl_name) {
        this.xl_name = xl_name;
    }

    public String getZy_name() {
        return zy_name;
    }

    public void setZy_name(String zy_name) {
        this.zy_name = zy_name;
    }

    public String getDj() {
        return dj;
    }

    public void setDj(String dj) {
        this.dj = dj;
    }

    public String getDq() {
        return dq;
    }

    public void setDq(String dq) {
        this.dq = dq;
    }

    @Override
    public String toString() {
        return "MyCompanyQyzzAllListBean{" +
                "lx_name='" + lx_name + '\'' +
                ", dl_name='" + dl_name + '\'' +
                ", xl_name='" + xl_name + '\'' +
                ", zy_name='" + zy_name + '\'' +
                ", dj='" + dj + '\'' +
                ", dq='" + dq + '\'' +
                '}';
    }
}
