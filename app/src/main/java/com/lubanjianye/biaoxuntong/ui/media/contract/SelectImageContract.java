package com.lubanjianye.biaoxuntong.ui.media.contract;

/**
 * Created by 11645 on 2018/1/26.
 */

public interface SelectImageContract {
    interface Operator {
        void requestCamera();

        void requestExternalStorage();

        void onBack();

        void setDataView(View view);
    }

    interface View {

        void onOpenCameraSuccess();

        void onCameraPermissionDenied();
    }
}
