package com.lubanjianye.biaoxuntong.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.ui.push.PushService;
import com.lzy.okgo.OkGo;
import com.mixpush.client.core.MixPushClient;
import com.mixpush.client.core.MixPushManager;
import com.mixpush.client.getui.GeTuiManager;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.shaohui.shareutil.ShareConfig;
import me.shaohui.shareutil.ShareManager;
import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;
import okhttp3.OkHttpClient;


public class App extends Application {
    //开发者模式开关
    public static boolean isDebug = true;


    @Override
    public void onCreate() {
        super.onCreate();

        //初始化Fragmentation
        initFragment();
        //初始化网络配置
        initNet();
        //初始化greenDao
        initGreenDao();
        //初始化推送
        initPush();
        // 初始化ShareUtil
        initShareUtil();
        //初始化okgo
        okgo();
        //初始化Arouter
        initArouter();

    }

    private void initArouter() {
        if (isDebug) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }

    private void initFragment() {
        Fragmentation.builder()
                // 设置 栈视图 模式为 悬浮球模式   SHAKE: 摇一摇唤出  默认NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.NONE)
                // 开发环境：true时，遇到异常："Can not perform this action after onSaveInstanceState!"时，抛出，并Crash;
                // 生产环境：false时，不抛出，不会Crash，会捕获，可以在handleException()里监听到
                .debug(true) // 实际场景建议.debug(BuildConfig.DEBUG)
                // 生产环境时，捕获上述异常（避免crash），会捕获
                // 建议在回调处上传下面异常到崩溃监控服务器
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
                        // Bugtags.sendException(e);
                    }
                })
                .install();
    }


    private void initNet() {
        //配置初始化
        BiaoXunTong.init(this)
                .withApiHost("http://api.lubanjianye.com/")
                .configure();
    }

    private void initGreenDao() {
        DatabaseManager.getInstance().init(this);
    }

    private void initPush() {
        MixPushClient.addPushManager(new GeTuiManager());
        MixPushClient.setPushIntentService(PushService.class);
        MixPushClient.setSelector(new MixPushClient.Selector() {
            @Override
            public String select(Map<String, MixPushManager> pushAdapterMap, String brand) {
                return GeTuiManager.NAME;
                //底层已经做了小米推送、魅族推送、个推判断，也可以按照自己的需求来选择推送
//                return super.select(pushAdapterMap, brand);
            }
        });
        // 配置接收推送消息的服务类
        MixPushClient.setPushIntentService(PushService.class);
        // 注册推送
        MixPushClient.registerPush(this);
        // 绑定别名，一般是填写用户的ID，便于定向推送
        MixPushClient.setAlias(this, getUserId());
        // 设置标签，用于对用户进行划分
        MixPushClient.setTags(this, "测试");


    }

    private String getUserId() {
        return "007";
    }

    private void initShareUtil() {

        ShareConfig config = ShareConfig.instance()
                .qqId("1106195613")
                .wxId("wxd7123ee6007bc26a")
                .weiboId("356365773")
                // 下面两个，如果不需要登录功能，可不填写
                .weiboRedirectUrl("https://api.weibo.com/oauth2/default.html")
                .wxSecret("5e85681e8a0c78d140dd7f49e4be3117");
        ShareManager.init(config);
    }

    private void okgo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //全局的读取超时时间
        builder.readTimeout(10000, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(10000, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS);

        OkGo.getInstance().init(this)
                .setOkHttpClient(builder.build())
                .setRetryCount(2);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
