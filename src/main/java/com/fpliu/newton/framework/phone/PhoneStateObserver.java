package com.fpliu.newton.framework.phone;


/**
 * 观察者
 * 
 * @author 792793182@qq.com 2015-03-03
 *
 */
public interface PhoneStateObserver {

    /**
     * 更新数据
     * @param observable  被观察者
     * @param isinComing  是否是来电，否则就是去电
     * @param phoneNumber 电话号码
     * @param phoneNumber 电话状态 @see TelephonyManager.CALL_STATE_RINGING
     */
    void update(PhoneStateObservable observable, boolean isinComing, String phoneNumber, int phoneState);
}
