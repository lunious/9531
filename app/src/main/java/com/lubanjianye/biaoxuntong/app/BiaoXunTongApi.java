package com.lubanjianye.biaoxuntong.app;


public abstract class BiaoXunTongApi {

    public static final String PAS_KEY = "老子不死你1";


    public static final String BASEURL = "http://api.lubanjianye.com/";
    /**
     * 一键已读
     */
    public static final String URL_YJYD = BASEURL + "bxtajax/GetuiTask/setState";

    /**
     * 推送列表
     */
    public static final String URL_GETUILIST = BASEURL + "bxtajax/GetuiTask/list";
    /**
     * 分享模板
     */
    public static final String SHARE_URL = BASEURL + "bxtajax/Entryajax/share?url=";
    /**
     * 得到首页Tab数据
     */
    public static final String URL_INDEXTAB = BASEURL + "bxtajax/key/ownerLabel";
    /**
     * 获取全部标签
     */
    public static final String URL_GETALLTAB = BASEURL + "bxtajax/key/jytype";
    /**
     * 上传头像
     */
    public static final String URL_UPTOUXIANG = BASEURL + "bxtajax/img/upload";
    /**
     * 设置标签顺序
     */
//    public static final String URL_TABLINE = BASEURL + "bxtajax/key/setLabelSort";

    public static final String URL_TABLINE = BASEURL + "bxtajax/key/setLabelSortTwo";

    /**
     * 账号、密码登陆
     */
    public static final String URL_LOGIN = BASEURL + "bxtajax/tokens/login";

    /**
     * 快捷登陆
     */
    public static final String URL_FASTLOGIN = BASEURL + "bxtajax/UserAjax/codeLogin";
    /**
     * 获取验证码
     */
    public static final String URL_GETCODE = BASEURL + "bxtajax/UserAjax/sendcode";
    /**
     * 获取用户个人资料
     */
    public static final String URL_GETUSERINFO = BASEURL + "bxtajax/UserAjax/getUserById";
    /**
     * 修改用户个人资料
     */
    public static final String URL_CHANGEUSER = BASEURL + "bxtajax/UserAjax/updateUserById";
    /**
     * 注册
     */
    public static final String URL_REGISTER = BASEURL + "bxtajax/UserAjax/register";
    /**
     * 修改密码
     */
    public static final String URL_FORGETPWD = BASEURL + "bxtajax/UserAjax/foegetPass";
    /**
     * 退出登陆
     */
    public static final String URL_LOGOUT = BASEURL + "bxtajax/tokens/logout";
    /**
     * 获得绑定企业名
     */
    public static final String URL_GETCOMPANYNAME = BASEURL + "bxtajax/TQyAjax/byQyId";
    /**
     * 获得收藏列表
     */
    public static final String URL_GETCOLLECTIONLIST = BASEURL + "bxtajax/FavoriteAjax/listAll";
    /**
     * 首页列表详情(加密)
     */
    public static final String URL_GETCOLLECTIONLISTDETAIL = BASEURL + "bxtajax/Entryajax/getDetailsDataSer";
    /**
     * 公共结果列表(加密)
     */
    public static final String URL_GETRESULTLIST = BASEURL + "bxtajax/Entryjgajax/getEntryjgListSer";
    /**
     * 公共结果列表详情(加密)
     */
    public static final String URL_GETRESULTLISTDETAIL = BASEURL + "bxtajax/Entryjgajax/getDetailsDataSer";
    /**
     * 首页公共列表(加密)
     */
    public static final String URL_GETINDEXLIST = BASEURL + "bxtajax/Entryajax/getEntryListSer";
    /**
     * 首页行业资讯列表
     */
    public static final String URL_GETINDEXHYZXLIST = BASEURL + "bxtajax/Entryajax/getDocList";
    /**
     * 模糊查询公司
     */
    public static final String URL_GETSUITCOMPANY = BASEURL + "bxtajax/TQyAjax/listAll";
    /**
     * 获得根据筛选条件查询到的结果
     */
    public static final String URL_SUITRESULT = "http://119.23.161.79/app/view/result";

    /**
     * 检查更新接口
     */
    public static final String URL_UPDATE = BASEURL + "bxtajax/VersionAjax/compareVersion";
    /**
     * 删除收藏
     */
    public static final String URL_DELEFAV = BASEURL + "bxtajax/FavoriteAjax/del";
    /**
     * 添加收藏
     */
    public static final String URL_ADDFAV = BASEURL + "bxtajax/FavoriteAjax/add";
    /**
     * QQ登录
     */
    public static final String URL_QQLOGIN = BASEURL + "bxtajax/tokens/inlogin";
    /**
     * 微博登录
     */
    public static final String URL_WEIBOLOGIN = BASEURL + "bxtajax/tokens/inlogin";
    /**
     * 微信登录
     */
    public static final String URL_WEIXINLOGIN = BASEURL + "bxtajax/tokens/inlogin";
    /**
     * 用户绑定公司
     */
    public static final String URL_USERBINDCOMPANY = BASEURL + "bxtajax/UserAjax/binding";
    /**
     * 得到全部企业资质
     */
    public static final String URL_GETALLCOMPANYQYZZ = BASEURL + "bxtajax/TQyzzQueryBxt/listAll";
    /**
     * 得到全部人员资质
     */
    public static final String URL_GETALLCOMPANYRYZZ = BASEURL + "bxtajax/TRyzzQueryBxt/listAll";
    /**
     * 获取资质类型（6种）
     */
    public static final String URL_GETZZLXLIST = "http://www.lubanjianye.com/qyzz_select/lx";
    public static final String URL_GETQYZZLIST = "http://www.lubanjianye.com/qyzz_select/branch";
    /**
     * 获取资质类型（全部）
     */
    public static final String URL_GETZZLXALLLIST = BASEURL + "bxtajax/ZzCodeajax/getAllZzcodeJs";

    /**
     * 获得符合筛选条件的企业Id
     */
    public static final String URL_GETSUITIDS = "http://119.23.161.79/app/query/qyzz";

    /**
     * 获得根据筛选条件查询到的结果
     */
    public static final String URL_GETSUITRESULT = "http://119.23.161.79/app/view/result";
    /**
     * 获得首页banner图
     */
    public static final String URL_GETINDEXBANNER = BASEURL + "bxtajax/bxtPicture/getAllpic";
    /**
     * 获得根据筛选条件查询到公司详情
     */
    public static final String URL_SUITRESULTDETAIl = "http://119.23.161.79/app/info/";
    /**
     * 获得根据筛选条件查询到公司企业资质
     */
    public static final String URL_COMPANYQYZZ = "http://119.23.161.79/app/detail/qyzz/";
    /**
     * 获得根据筛选条件查询到公司人员资质
     */
    public static final String URL_COMPANYRYZZ = "http://119.23.161.79/app/detail/ryzz/";
    /**
     * 绑定手机号
     */
    public static final String URL_BINDMOBILE = BASEURL + "bxtajax/UserAjax/bindingMoblie";
    /**
     * 获得根据筛选条件查询到公司施工业绩
     */
    public static final String URL_COMPANYSGYJ = "http://119.23.161.79/app/detail/gcyj/";
    /**
     * 获得根据筛选条件查询到公司信用奖惩
     */
    public static final String URL_COMPANYXYJC = "http://119.23.161.79/app/detail/xyjc/";
    /**
     * 添加标签
     */
    public static final String URL_ADDITEMLABEL = BASEURL + "bxtajax/key/addLabel";
    /**
     * 删除标签
     */
    public static final String URL_DELEITEMLABEL = BASEURL + "bxtajax/key/deleteLabel";
    /**
     * 手动添加企业资质信息（企业资质维护）
     */
    public static final String URL_ADDQYZZ = BASEURL + "bxtajax/TQyzzQueryBxt/add";
    /**
     * 删除企业资质
     */
    public static final String URL_DELEQYZZ = BASEURL + "bxtajax/TQyzzQueryBxt/setDelete";
    /**
     * 获取人员类型（全部）
     */
    public static final String URL_GETALLRYLX = BASEURL + "bxtajax/ZzCodeajax/getAllRyzzcodeJs";
    /**
     * 手动添加人员资质信息（企业资质维护）
     */
    public static final String URL_ADDRYZZ = BASEURL + "bxtajax/TRyzzQueryBxt/add";
    /**
     * 删除人员资质
     */
    public static final String URL_DELERYZZ = BASEURL + "bxtajax/TRyzzQueryBxt/setDelete";
    /**
     * 验证token是否有效
     */
    public static final String URL_CHECKTOKEN = BASEURL + "bxtajax/tokens/checkToken";
    /**
     * 用户行为
     */
    public static final String URL_GETUITASK = BASEURL + "bxtajax/GetuiTask/onclick";
    /**
     * 发送企业报告
     */
    public static final String URL_SENDPDF = "http://119.23.161.79/app/downpdf/";


}
