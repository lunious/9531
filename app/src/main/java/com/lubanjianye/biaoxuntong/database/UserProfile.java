package com.lubanjianye.biaoxuntong.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;


/**
 * 项目名:   LuBanBiaoXunTong
 * 包名:     com.lubanjianye.biaoxuntong.pw.database
 * 文件名:   UserProfile
 * 创建者:   lunious
 * 创建时间: 2017/10/16  23:30
 * 描述:     TODO
 */

@Entity(nameInDb = "user_profile")
public class UserProfile {
    @Id
    private long id = 0;
    private String mobile = null;
    private String nickName = null;
    private String token = null;
    private String comid = null;
    private String ImageUrl = null;
    private String companyName = null;

    @Generated(hash = 1554870577)
    public UserProfile(long id, String mobile, String nickName, String token,
                       String comid, String ImageUrl, String companyName) {
        this.id = id;
        this.mobile = mobile;
        this.nickName = nickName;
        this.token = token;
        this.comid = comid;
        this.ImageUrl = ImageUrl;
        this.companyName = companyName;
    }

    @Generated(hash = 968487393)
    public UserProfile() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getComid() {
        return this.comid;
    }

    public void setComid(String comid) {
        this.comid = comid;
    }

    public String getImageUrl() {
        return this.ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


}
