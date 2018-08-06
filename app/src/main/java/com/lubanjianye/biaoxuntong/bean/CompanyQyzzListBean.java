package com.lubanjianye.biaoxuntong.bean;

import java.io.Serializable;

/**
 * 项目名:   Lunious
 * 包名:     com.lubanjianye.biaoxuntong.bean
 * 文件名:   CompanyQyzzListBean
 * 创建者:   lunious
 * 创建时间: 2017/11/30  10:53
 * 描述:     TODO
 */

public class CompanyQyzzListBean implements Serializable{

    private String lx = null;
    private String zzmc = null;
    private String zsh = null;

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getZzmc() {
        return zzmc;
    }

    public void setZzmc(String zzmc) {
        this.zzmc = zzmc;
    }

    public String getZsh() {
        return zsh;
    }

    public void setZsh(String zsh) {
        this.zsh = zsh;
    }

    @Override
    public String toString() {
        return "CompanyQyzzListBean{" +
                "lx='" + lx + '\'' +
                ", zzmc='" + zzmc + '\'' +
                ", zsh='" + zsh + '\'' +
                '}';
    }
}
