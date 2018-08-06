package com.lubanjianye.biaoxuntong.ui.share;


public interface OpenConstant {

    int WECHAT = 0;
    int TENCENT = 1;
    int SINA = 2;

    String WECHAT_APP_ID = "wxd7123ee6007bc26a";
    String WECHAT_SECRET = "5e85681e8a0c78d140dd7f49e4be3117";

    String QQ_APP_ID = "1106195613";
    String QQ_APP_KEY = "gmA4qrPn1IDhnOaN";

    String WB_APP_ID = "524298520";
    String WB_APP_KEY = "356365773";
    String WB_SECRET = "90a9fe67427c7ece190940079f231571";

    /**
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * <p>
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * <p>
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * <p>
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog,"+ "invitation_write";//最后一个为好友邀请功能

}
