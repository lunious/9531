package com.lubanjianye.biaoxuntong.bean;

import java.io.Serializable;

/**
 * 项目名:   Lunioussky
 * 包名:     com.lubanjianye.biaoxuntong.bean
 * 文件名:   Version
 * 创建者:   lunious
 * 创建时间: 2017/10/27  23:52
 * 描述:     TODO
 */

public class Version implements Serializable {

    private String status = null;
    private String message = null;
    private String data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
