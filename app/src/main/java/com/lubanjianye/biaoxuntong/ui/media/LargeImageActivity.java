package com.lubanjianye.biaoxuntong.ui.media;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.app.BiaoXunTong;
import com.lubanjianye.biaoxuntong.ui.media.util.Util;
import com.lubanjianye.biaoxuntong.util.BitmapUtil;
import com.lubanjianye.biaoxuntong.util.Loading;
import com.lubanjianye.biaoxuntong.util.StreamUtil;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by 11645 on 2018/1/26.
 */

public class LargeImageActivity extends SwipeBackActivity implements EasyPermissions.PermissionCallbacks, View.OnClickListener {


    private SubsamplingScaleImageView mImageView;
    private ImageView mImageSave;
    private Loading mLoading;


    private String mPath;

    public static void show(Context context, String image) {
        Intent intent = new Intent(context, LargeImageActivity.class);
        intent.putExtra("image", image);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image);

        mImageView = (SubsamplingScaleImageView) findViewById(R.id.imageView);
        mImageSave = (ImageView) findViewById(R.id.iv_save);
        mLoading = (Loading) findViewById(R.id.loading);
        mImageSave.setOnClickListener(this);


        initWidget();
        initData();
    }

    protected void initWidget() {
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mImageView.setMaxScale(15);
        mImageView.setZoomEnabled(true);
        mImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    protected RequestManager mImageLoader;

    public synchronized RequestManager getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = Glide.with(this);
        }

        return mImageLoader;
    }

    protected void initData() {
        mPath = getIntent().getStringExtra("image");
        getImageLoader()
                .load(mPath)
                .downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                        if (isDestroyed()){
                            return;
                        }
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(resource.getPath(), options);
                        int w = options.outWidth;
                        int sw = Util.getScreenWidth(LargeImageActivity.this);
                        float scale = (float) sw / (float) w;
                        mImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
                        mImageView.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(scale,
                                new PointF(0, 0), BitmapUtil.readPictureDegree(mPath)));
                        mImageSave.setVisibility(View.VISIBLE);
                        mLoading.stop();
                        mLoading.setVisibility(View.GONE);
                    }
                });
    }

    private static final int PERMISSION_ID = 0x0001;

    @SuppressWarnings("unused")
    @AfterPermissionGranted(PERMISSION_ID)
    public void saveToFileByPermission() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, permissions)) {
            saveToFile();
        } else {
            EasyPermissions.requestPermissions(this, "请授予保存图片权限", PERMISSION_ID, permissions);
        }
    }

    private void saveToFile() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "没有外部存储", Toast.LENGTH_SHORT).show();
            return;
        }

        final Future<File> future = getImageLoader()
                .load(mPath)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);


        BiaoXunTong.getHandler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    File sourceFile = future.get();
                    if (sourceFile == null || !sourceFile.exists()){
                        return;
                    }
                    String extension = BitmapUtil.getExtension(sourceFile.getAbsolutePath());
                    String extDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .getAbsolutePath() + File.separator + "标讯通";
                    File extDirFile = new File(extDir);
                    if (!extDirFile.exists()) {
                        if (!extDirFile.mkdirs()) {
                            // If mk dir error
                            callSaveStatus(false, null);
                            return;
                        }
                    }
                    final File saveFile = new File(extDirFile, String.format("IMG_%s.%s", System.currentTimeMillis(), extension));
                    final boolean isSuccess = StreamUtil.copyFile(sourceFile, saveFile);
                    callSaveStatus(isSuccess, saveFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    callSaveStatus(false, null);
                }
            }
        });

    }

    private void callSaveStatus(final boolean success, final File savePath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    // notify
                    if (isDestroyed()){
                        return;
                    }
                    Uri uri = Uri.fromFile(savePath);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    Toast.makeText(LargeImageActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LargeImageActivity.this, "存储失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this, "没有外部存储权限", Toast.LENGTH_SHORT).show();
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_save:
                saveToFileByPermission();
                break;
            default:
                break;
        }
    }
}
