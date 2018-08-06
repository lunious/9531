package com.lubanjianye.biaoxuntong.bean;

import java.io.Serializable;

/**
 * 项目名:   Lunious
 * 包名:     com.lubanjianye.biaoxuntong.bean
 * 文件名:   MyCompanyRyzzAllListBean
 * 创建者:   lunious
 * 创建时间: 2017/11/30  14:21
 * 描述:     TODO
 */

public class MyCompanyRyzzAllListBean implements Serializable{

    private String lx_name = null;
    private String zg_name = null;
    private String zg_mcdj = null;
    private String zgzy = null;
    private String ryname = null;


    public String getLx_name() {
        return lx_name;
    }

    public void setLx_name(String lx_name) {
        this.lx_name = lx_name;
    }

    public String getZg_name() {
        return zg_name;
    }

    public void setZg_name(String zg_name) {
        this.zg_name = zg_name;
    }

    public String getZg_mcdj() {
        return zg_mcdj;
    }

    public void setZg_mcdj(String zg_mcdj) {
        this.zg_mcdj = zg_mcdj;
    }

    public String getZgzy() {
        return zgzy;
    }

    public void setZgzy(String zgzy) {
        this.zgzy = zgzy;
    }

    public String getRyname() {
        return ryname;
    }

    public void setRyname(String ryname) {
        this.ryname = ryname;
    }

    @Override
    public String toString() {
        return "MyCompanyRyzzAllListBean{" +
                "lx_name='" + lx_name + '\'' +
                ", zg_name='" + zg_name + '\'' +
                ", zg_mcdj='" + zg_mcdj + '\'' +
                ", zgzy='" + zgzy + '\'' +
                ", ryname='" + ryname + '\'' +
                '}';
    }
}
