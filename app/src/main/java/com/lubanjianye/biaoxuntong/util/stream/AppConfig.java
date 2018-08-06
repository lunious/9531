package com.lubanjianye.biaoxuntong.util.stream;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.lubanjianye.biaoxuntong.app.BiaoXunTong;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.util.stream
 * 文件名:   AppConfig
 * 创建者:   lunious
 * 创建时间: 2017/12/14  19:19
 * 描述:     TODO
 */

public class AppConfig {


    private final static String APP_CONFIG = "config";
    public static final String KEY_LOAD_IMAGE = "KEY_LOAD_IMAGE";
    public static final String KEY_CHECK_UPDATE = "KEY_CHECK_UPDATE";
    public static final String KEY_DOUBLE_CLICK_EXIT = "KEY_DOUBLE_CLICK_EXIT";

    // 默认存放图片的路径
    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "BiaoXunTong"
            + File.separator + "luban_img" + File.separator;

    // 默认存放文件下载的路径
    public final static String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "BiaoXunTong"
            + File.separator + "download" + File.separator;

    private Context mContext;
    private static AppConfig appConfig;

    public static AppConfig getAppConfig(Context context) {
        if (appConfig == null) {
            appConfig = new AppConfig();
            appConfig.mContext = context;
        }
        return appConfig;
    }

    public String get(String key) {
        Properties props = get();
        return (props != null) ? props.getProperty(key) : null;
    }

    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            // 读取app_config目录下的config
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator
                    + APP_CONFIG);

            props.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(fis);
        }
        return props;
    }

    private void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
            // 把config建在(自定义)app_config的目录下
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File conf = new File(dirConf, APP_CONFIG);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(fos);
        }
    }

    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    public void remove(String... key) {
        Properties props = get();
        for (String k : key)
            props.remove(k);
        setProps(props);
    }

    @SuppressWarnings("deprecation")
    public static void copyTextToBoard(String string) {
        if (TextUtils.isEmpty(string)) {
            return;
        }
        ClipboardManager clip = (ClipboardManager) BiaoXunTong.getApplicationContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(string);
    }

    /**
     * 打开外置的浏览器
     *
     * @param context
     * @param url
     */
    public static void openExternalBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(Intent.createChooser(intent, "选择打开的应用"));
    }



}
