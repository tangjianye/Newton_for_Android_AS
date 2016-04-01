package com.fpliu.newton.framework.phone;

import java.util.ArrayList;
import java.util.List;

/**
 * 被观察者
 * 
 * @author 792793182@qq.com 2015-03-03
 *
 */
public class PhoneStateObservable {

	private List<PhoneStateObserver> observers = new ArrayList<PhoneStateObserver>();

	private boolean changed = false;

    /**
     * 添加观察者
     * @param observer 观察者
     */
    public void addObserver(PhoneStateObserver observer) {
        synchronized (this) {
            if (!observers.contains(observer)) {
            	observers.add(observer);
            }
        }
    }

    /**
     * 清除数据变化的标志
     */
    protected void clearChanged() {
        changed = false;
    }

    /**
     * 计算观察者的数量
     */
    public int countObservers() {
        return observers.size();
    }

    /**
     * 删除观察者
     * @param observer 观察者
     */
    public synchronized void deleteObserver(PhoneStateObserver observer) {
        observers.remove(observer);
    }

    /**
     * 删除所有的观察者
     */
    public synchronized void deleteObservers() {
        observers.clear();
    }

    /**
     * 判断数据是否变化了
     */
    public boolean hasChanged() {
        return changed;
    }

    /**
     * 通知所有的观察者
     * @param isinComing  是否是来电，否则就是去电
     * @param phoneNumber 电话号码
     * @param phoneNumber 电话状态 @see TelephonyManager.CALL_STATE_RINGING
     */
    public void notifyObservers(boolean isinComing, String phoneNumber, int phoneState) {
        int size = 0;
        PhoneStateObserver[] arrays = null;
        synchronized (this) {
            if (hasChanged()) {
                clearChanged();
                size = observers.size();
                arrays = new PhoneStateObserver[size];
                observers.toArray(arrays);
            }
        }
        if (arrays != null) {
            for (PhoneStateObserver observer : arrays) {
                observer.update(this, isinComing, phoneNumber, phoneState);
            }
        }
    }

    /**
     * 标志为数据发生变化了
     */
    public void setChanged() {
        changed = true;
    }
}
