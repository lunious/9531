package com.lubanjianye.biaoxuntong.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ContentFrameLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;


public abstract class BaseActivity1 extends SwipeBackActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化，默认透明状态栏和黑色导航栏
        ImmersionBar.with(this).init();
        initContainer(savedInstanceState);
        initToggton();
    }

    //用来容纳Fragment的容器
    private void initContainer(@Nullable Bundle savedInstanceState) {
        @SuppressLint("RestrictedApi") final ContentFrameLayout container = new ContentFrameLayout(this);
        container.setId(R.id.fragment_container);
        setContentView(container);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fragment_container, setRootFragment());
        }
    }

    public abstract BaseFragment1 setRootFragment();

    private void initToggton() {

        if (AppSharePreferenceMgr.contains(this, EventMessage.LEFT_BACK)) {
            setSwipeBackEnable(false);
        } else {
//            setSwipeBackEnable(true);
        }

    }


    @Override
    protected void onDestroy() {
        //垃圾回收
        System.gc();
        System.runFinalization();
        super.onDestroy();
    }

    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }


}
