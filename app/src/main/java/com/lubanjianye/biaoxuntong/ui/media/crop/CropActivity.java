package com.lubanjianye.biaoxuntong.ui.media.crop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.ui.media.config.SelectOptions;
import com.lubanjianye.biaoxuntong.util.StreamUtil;

import java.io.FileOutputStream;

import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

/**
 * Created by 11645 on 2018/1/26.
 */

public class CropActivity extends SwipeBackActivity implements View.OnClickListener {

    private CropLayout mCropLayout;
    private static SelectOptions mOption;

    public static void show(Fragment fragment, SelectOptions options) {
        Intent intent = new Intent(fragment.getActivity(), CropActivity.class);
        mOption = options;
        fragment.startActivityForResult(intent, 0x04);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);


        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mCropLayout = (CropLayout) findViewById(R.id.cropLayout);

        String url = mOption.getSelectedImages().get(0);
        getImageLoader().load(url)
                .fitCenter()
                .into(mCropLayout.getImageView());

        mCropLayout.setCropWidth(mOption.getCropWidth());
        mCropLayout.setCropHeight(mOption.getCropHeight());
        mCropLayout.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_crop:
                Bitmap bitmap = null;
                FileOutputStream os = null;
                try {
                    bitmap = mCropLayout.cropBitmap();
                    String path = getFilesDir() + "/crop.jpg";
                    os = new FileOutputStream(path);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.flush();
                    os.close();

                    Intent intent = new Intent();
                    intent.putExtra("crop_path", path);
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bitmap != null) bitmap.recycle();
                    StreamUtil.close(os);
                }
                break;
            case R.id.tv_cancel:
                finish();
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        mOption = null;
        super.onDestroy();
    }

    protected RequestManager mImageLoader;

    public synchronized RequestManager getImageLoader() {
        if (mImageLoader == null)
            mImageLoader = Glide.with(this);
        return mImageLoader;
    }
}
