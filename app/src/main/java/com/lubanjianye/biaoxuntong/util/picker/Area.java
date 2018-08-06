package com.lubanjianye.biaoxuntong.util.picker;

import java.io.Serializable;

/**
 * Created by lunious on 2018/3/31.
 * Desc:
 */
public class Area implements Serializable {
    private int id;
    private String name;

    public Area(int id, String name) {
        this.id = id;
        this.name = name;
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
        //重写该方法，作为选择器显示的名称
        return name;
    }

}
