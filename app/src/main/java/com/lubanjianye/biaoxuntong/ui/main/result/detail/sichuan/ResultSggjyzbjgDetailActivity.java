package com.lubanjianye.biaoxuntong.ui.main.result.detail.sichuan;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.lubanjianye.biaoxuntong.base.BaseActivity1;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

/**
 * 项目名:   AppLunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.result.detail
 * 文件名:   ResultSggjyzbjgDetailActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/12  21:28
 * 描述:     TODO
 */

public class ResultSggjyzbjgDetailActivity extends BaseActivity1 {

    private int mEntityId = -1;
    private String mEntity = "";
    private String ajaxType = "";
    private String mId = "";


    @Override
    public BaseFragment1 setRootFragment() {
        Intent intent = getIntent();
        if (intent != null) {
            mEntityId = intent.getIntExtra("entityId", -1);
            mEntity = intent.getStringExtra("entity");
            ajaxType = intent.getStringExtra("ajaxlogtype");
            mId = intent.getStringExtra("mId");
        }

        Log.d("JABNDJBSJDJASDA", mId+mEntityId+mEntity);
        if (!TextUtils.isEmpty(mId)) {

            OkGo.<String>post(BiaoXunTongApi.URL_GETUITASK)
                    .params("type", 2)
                    .params("id", mId)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                        }
                    });

        }

        final ResultSggjyzbjgDetailFragment fragment = ResultSggjyzbjgDetailFragment.create(mEntityId, mEntity, ajaxType);
        return fragment;
    }

}
