package com.lubanjianye.biaoxuntong.pay;

/**
 * Author: lunious
 * Date: 2018/8/7 23:03
 * Description:
 */
public interface PayResultCallBack {
    /**
     * 支付成功
     */
    void onSuccess();

    /**
     * 正在处理中 小概率事件 此时以验证服务端异步通知结果为准
     */
    void onDealing();

    /**
     * 支付失败
     *
     * @param error_code 错误码
     */
    void onError(int error_code);

    /**
     * 支付取消
     */
    void onCancel();
}