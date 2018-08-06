package com.lubanjianye.biaoxuntong.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lubanjianye.biaoxuntong.ui.share.ShareDialog;
import com.lubanjianye.biaoxuntong.util.HTMLUtil;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;


public abstract class BaseFragment1 extends SwipeBackFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = null;
        if (setLayout() instanceof Integer) {
            rootView = inflater.inflate((Integer) setLayout(), container, false);
        } else if (setLayout() instanceof View) {
            rootView = (View) setLayout();
        }
        return rootView;
    }

    public final BaseActivity1 getProxyActivity() {
        return (BaseActivity1) _mActivity;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    public abstract Object setLayout();

    public abstract void initView();

    public abstract void initData();

    public abstract void initEvent();


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
                    ShareDialog(getActivity(), id)
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
