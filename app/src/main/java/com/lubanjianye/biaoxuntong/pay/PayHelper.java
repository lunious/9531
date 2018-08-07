package com.lubanjianye.biaoxuntong.pay;


import android.content.Context;

/**
 * Author: lunious
 * Date: 2018/8/7 23:01
 * Description:
 */
public class PayHelper {

    private static class PayHelperHolder {
        private static final PayHelper instance = new PayHelper();
    }

    private PayHelper() {

    }

    public static final PayHelper getInstance() {
        return PayHelperHolder.instance;
    }

    /**
     * 发起微信支付
     *
     * @param context 上下文
     * @param payParams 支付参数对象
     * @param callBack  回调
     */
    public void doWXPay(Context context, WXPayParam payParams, PayResultCallBack callBack) {
        WXPay.getInstance().init(context).doPay(payParams, callBack);
    }

}