package com.lubanjianye.biaoxuntong.ui.main.user.opinion;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseActivity;
import com.lubanjianye.biaoxuntong.ui.media.SelectImageActivity;
import com.lubanjianye.biaoxuntong.ui.media.config.SelectOptions;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 11645 on 2018/1/25.
 */

@Route(path = "/com/OpinionActivity")
public class OpinionActivity extends BaseActivity {

    @BindView(R.id.ll_iv_back)
    LinearLayout llIvBack;
    @BindView(R.id.main_bar_name)
    AppCompatTextView mainBarName;
    @BindView(R.id.message_num)
    AppCompatTextView messageNum;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;
    @BindView(R.id.rb_error)
    RadioButton rbError;
    @BindView(R.id.et_feed_back)
    EditText etFeedBack;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.iv_clear_img)
    ImageView ivClearImg;
    @BindView(R.id.tv_commit)
    AppCompatTextView tvCommit;


    private String mFilePath = "";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_opinion;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("意见反馈");
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        rbError.setChecked(true);
    }


    @OnClick({R.id.ll_iv_back, R.id.iv_add, R.id.iv_clear_img, R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                finish();
                break;
            case R.id.iv_add:
                openImageSelector();
                break;
            case R.id.iv_clear_img:
                ivAdd.setImageResource(R.mipmap.ic_tweet_add);
                ivClearImg.setVisibility(View.GONE);
                mFilePath = "";
                break;
            case R.id.tv_commit:
                String content = etFeedBack.getText().toString().trim();
                if (TextUtils.isEmpty(content) && TextUtils.isEmpty(mFilePath)) {
                    ToastUtil.shortToast(this, "内容为空");
                    return;
                }
                ToastUtil.shortToast(this, "感谢您的反馈！");
                finish();
                break;
        }
    }

    protected RequestManager mImageLoader;

    public synchronized RequestManager getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = Glide.with(this);
        }
        return mImageLoader;
    }

    public void openImageSelector() {
        SelectImageActivity.show(this, new SelectOptions.Builder()
                .setHasCam(false)
                .setSelectCount(1)
                .setCallback(new SelectOptions.Callback() {
                    @Override
                    public void doSelected(String[] images) {
                        mFilePath = images[0];
                        getImageLoader().load(mFilePath).into(ivAdd);
                        ivClearImg.setVisibility(View.VISIBLE);
                    }
                }).build());
    }

}
