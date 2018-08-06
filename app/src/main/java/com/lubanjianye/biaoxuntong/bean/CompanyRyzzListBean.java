package com.lubanjianye.biaoxuntong.bean;

import java.io.Serializable;

/**
 * 项目名:   Lunious
 * 包名:     com.lubanjianye.biaoxuntong.bean
 * 文件名:   CompanyRyzzListBean
 * 创建者:   lunious
 * 创建时间: 2017/11/30  12:46
 * 描述:     TODO
 */

public class CompanyRyzzListBean implements Serializable{

    private String ry = null;
    private String sfz = null;
    private String zgMcdj = null;
    private String zgZy = null;
    private String zsh = null;

    public String getRy() {
        return ry;
    }

    public void setRy(String ry) {
        this.ry = ry;
    }

    public String getSfz() {
        return sfz;
    }

    public void setSfz(String sfz) {
        this.sfz = sfz;
    }

    public String getZgMcdj() {
        return zgMcdj;
    }

    public void setZgMcdj(String zgMcdj) {
        this.zgMcdj = zgMcdj;
    }

    public String getZgZy() {
        return zgZy;
    }

    public void setZgZy(String zgZy) {
        this.zgZy = zgZy;
    }

    public String getZsh() {
        return zsh;
    }

    public void setZsh(String zsh) {
        this.zsh = zsh;
    }

    @Override
    public String toString() {
        return "CompanyRyzzListBean{" +
                "ry='" + ry + '\'' +
                ", sfz='" + sfz + '\'' +
                ", zgMcdj='" + zgMcdj + '\'' +
                ", zgZy='" + zgZy + '\'' +
                ", zsh='" + zsh + '\'' +
                '}';
    }
}
