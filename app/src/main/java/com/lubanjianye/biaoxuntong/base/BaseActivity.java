package com.lubanjianye.biaoxuntong.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.barlibrary.ImmersionBar;
import com.lubanjianye.biaoxuntong.ui.share.ShareDialog;
import com.lubanjianye.biaoxuntong.util.HTMLUtil;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

/**
 * Author: lunious
 * Date: 2018/7/16 13:44
 * Description:
 */
public abstract class BaseActivity extends SwipeBackActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化Arouter注解方法
        ARouter.getInstance().inject(this);
        //初始化，默认透明状态栏和黑色导航栏
        ImmersionBar.with(this).init();
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        initData(savedInstanceState);
        initEvent(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ImmersionBar.with(this).destroy();
        //垃圾回收
        System.gc();
        System.runFinalization();
        unbinder.unbind();
        super.onDestroy();

    }


    /**
     * 布局
     */
    protected abstract int getLayoutId();

    /**
     * 数据
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 事件
     */
    protected abstract void initEvent(Bundle savedInstanceState);


    protected ShareDialog mAlertDialog;

    @SuppressWarnings({"LoopStatementThatDoesntLoop", "SuspiciousMethodCalls"})
    protected void toShare(int id, String title, String content, String url) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(url)) {
            return;
        }
        String imageUrl = null;
        content = content.trim();
        if (content.length() > 55) {
            content = content.substring(0, 55);

        } else {
            content = HTMLUtil.delHTMLTag(content);
        }
        if (TextUtils.isEmpty(content)) {
            content = "";
        }

        // 分享
        if (mAlertDialog == null) {
            mAlertDialog = new
                    ShareDialog(this, id)
                    .type(id)
                    .title(title)
                    .content(content)
                    .imageUrl(imageUrl)//如果没有图片，即url为null，直接加入app默认分享icon
                    .url(url).with();
        }
        mAlertDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAlertDialog != null) {
            mAlertDialog.hideProgressDialog();
        }
    }
}
