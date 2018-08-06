package com.lubanjianye.biaoxuntong.bean;

import java.io.Serializable;


public class CompanySearchResultListBean implements Serializable{

    private String qy = null;
    private String lxr = null;
    private String entrySign = null;
    private String sfId = null;
    private String showSign = null;
    private String provinceCode = null;

    public String getShowSign() {
        return showSign;
    }

    public void setShowSign(String showSign) {
        this.showSign = showSign;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getQy() {
        return qy;
    }

    public void setQy(String qy) {
        this.qy = qy;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    public String getEntrySign() {
        return entrySign;
    }

    public void setEntrySign(String entrySign) {
        this.entrySign = entrySign;
    }

    public String getSfId() {
        return sfId;
    }

    public void setSfId(String sfId) {
        this.sfId = sfId;
    }

    @Override
    public String toString() {
        return "CompanySearchResultListBean{" +
                "qy='" + qy + '\'' +
                ", lxr='" + lxr + '\'' +
                ", entrySign='" + entrySign + '\'' +
                ", sfId='" + sfId + '\'' +
                '}';
    }
}
