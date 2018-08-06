package com.lubanjianye.biaoxuntong.ui.main.index.detail.sichuan;

import android.content.Intent;
import android.text.TextUtils;

import com.lubanjianye.biaoxuntong.base.BaseActivity1;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

/**
 * 项目名:   Lunious
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.index.detail
 * 文件名:   IndexSggjycgtableDetailActivity
 * 创建者:   lunious
 * 创建时间: 2017/12/7  15:27
 * 描述:     TODO
 */

public class IndexSggjycgtableDetailActivity extends BaseActivity1 {
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

        final IndexSggjycgtableDetailFragment fragment = IndexSggjycgtableDetailFragment.create(mEntityId, mEntity, ajaxType);
        return fragment;
    }
}
