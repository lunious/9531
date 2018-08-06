package com.lubanjianye.biaoxuntong.bean;

import java.io.Serializable;

/**
 * 项目名:   Lunious
 * 包名:     com.lubanjianye.biaoxuntong.bean
 * 文件名:   IndexHyzxListBean
 * 创建者:   lunious
 * 创建时间: 2017/11/30  9:48
 * 描述:     TODO
 */

public class IndexHyzxListBean implements Serializable{

    private int id = 0;
    private String title = null;
    private String mobile_img = null;
    private String create_time = null;
    private String mobile_context = null;
    private int img = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMobile_img() {
        return mobile_img;
    }

    public void setMobile_img(String mobile_img) {
        this.mobile_img = mobile_img;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getMobile_context() {
        return mobile_context;
    }

    public void setMobile_context(String mobile_context) {
        this.mobile_context = mobile_context;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "IndexHyzxListBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", mobile_img='" + mobile_img + '\'' +
                ", create_time='" + create_time + '\'' +
                ", mobile_context='" + mobile_context + '\'' +
                ", img=" + img +
                '}';
    }
}
