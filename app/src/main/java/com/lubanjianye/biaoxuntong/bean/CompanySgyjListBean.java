package com.lubanjianye.biaoxuntong.bean;

import java.io.Serializable;

/**
 * 项目名:   Lunious
 * 包名:     com.lubanjianye.biaoxuntong.bean
 * 文件名:   CompanySgyjListBean
 * 创建者:   lunious
 * 创建时间: 2017/11/30  13:42
 * 描述:     TODO
 */

public class CompanySgyjListBean implements Serializable{

    private String xmmc = null;
    private String zbje = null;
    private String zbsj = null;
    private String xmfzr = null;

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public String getZbje() {
        return zbje;
    }

    public void setZbje(String zbje) {
        this.zbje = zbje;
    }

    public String getZbsj() {
        return zbsj;
    }

    public void setZbsj(String zbsj) {
        this.zbsj = zbsj;
    }

    public String getXmfzr() {
        return xmfzr;
    }

    public void setXmfzr(String xmfzr) {
        this.xmfzr = xmfzr;
    }

    @Override
    public String toString() {
        return "CompanySgyjListBean{" +
                "xmmc='" + xmmc + '\'' +
                ", zbje='" + zbje + '\'' +
                ", zbsj='" + zbsj + '\'' +
                ", xmfzr='" + xmfzr + '\'' +
                '}';
    }
}
