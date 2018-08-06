package com.lubanjianye.biaoxuntong.ui.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.base.MainActivity;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.eventbus.EventMessage;
import com.lubanjianye.biaoxuntong.util.dimen.DensityUtil;
import com.lubanjianye.biaoxuntong.util.sp.AppSharePreferenceMgr;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lubanjianye.biaoxuntong.app.BiaoXunTong.getApplicationContext;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.launcher
 * 文件名:   LauncherScrollActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/10  22:51
 * 描述:     TODO
 */

@Route(path = "/com/LauncherScrollActivity")
public class LauncherScrollActivity extends BaseActivity {


    @BindView(R.id.vp_guide_pages)
    ViewPager vpGuidePages;
    @BindView(R.id.ll_guide_points)
    LinearLayout llGuidePoints;
    @BindView(R.id.v_guide_redpoint)
    View vGuideRedpoint;
    @BindView(R.id.btn_guide_startexp)
    Button btnGuideStartexp;


    private List<ImageView> guides;
    private MyViewPageAdapter viewPageAdapter;
    //点与点之间的距离
    private int disPoints;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_launcher_scroll;
    }


    private long id = 0;
    @Override
    protected void initData(Bundle savedInstanceState) {
        List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
        for (int i = 0; i < users.size(); i++) {
            id = users.get(0).getId();
        }
        if (id > 0) {
            AppSharePreferenceMgr.put(this, EventMessage.LOGIN_SUCCSS, true);
            EventBus.getDefault().post(new EventMessage(EventMessage.LOGIN_SUCCSS));
        }
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        //viewpage adapter list
        //图片数据
        int[] pics = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
        //定义ViewPage使用的容器
        guides = new ArrayList<ImageView>();
        //初始化容器中的数据
        for (int i = 0; i < pics.length; i++) {
            ImageView iv_temp = new ImageView(getApplicationContext());
            iv_temp.setBackgroundResource(pics[i]);
            //添加界面的数据
            guides.add(iv_temp);
            //给点的容器LinearLayout初始化添加灰色的点
            View v_point = new View(getApplicationContext());
            v_point.setBackgroundResource(R.drawable.gray_point);

            int dip = 10;
            //设置灰色点的大小
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(getApplicationContext(), dip), DensityUtil.dip2px(getApplicationContext(), dip));//单位是px
            //设置点与点之间的空隙
            if (i != 0)//过滤第一个点
                params.leftMargin = 38;//px
            v_point.setLayoutParams(params);
            //添加灰色的点到线性布局中
            llGuidePoints.addView(v_point);
        }


        //创建Viewpaged的适配器
        viewPageAdapter = new MyViewPageAdapter();
        //设置适配器
        vpGuidePages.setAdapter(viewPageAdapter);


        //监听布局完成触发的结果
        vGuideRedpoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //取消注册界面变化而发生的回调结果
                vGuideRedpoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //计算点与点之间的距离
                disPoints = (llGuidePoints.getChildAt(1).getLeft() - llGuidePoints.getChildAt(0).getLeft());

            }
        });


        //给按钮添加点击事件
        btnGuideStartexp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppSharePreferenceMgr.put(LauncherScrollActivity.this, "first_into_app", true);
                //进入主页
//                ARouter.getInstance().build("/com/MainActivity").navigation();
                startActivity(new Intent(LauncherScrollActivity.this, MainActivity.class));
                finish();

            }
        });

        //给Viewpager添加页码改变的事件
        vpGuidePages.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //positionOffset 移动的比例值
                //计算红点的左边距
                float leftMargin = disPoints * (position + positionOffset);
                //设置红点的左边距
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vGuideRedpoint.getLayoutParams();
                layoutParams.leftMargin = Math.round(leftMargin);//对float类型四舍五入

                //重新设置布局
                vGuideRedpoint.setLayoutParams(layoutParams);

            }

            @Override
            public void onPageSelected(int position) {
                //当前ViewPager显示的页码
                if (position == guides.size() - 1) {
                    //当ViewPager滑动到最后一页时，显示Button
                    btnGuideStartexp.setVisibility(View.VISIBLE);
                } else {
                    //不是最后一页，隐藏button
                    btnGuideStartexp.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class MyViewPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return guides.size();//返回数据的个数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //container viewpager
            //获取view
            View child = guides.get(position);
            //添加view
            container.addView(child);
            return child;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);//从viewpage中移除
        }
    }


}
