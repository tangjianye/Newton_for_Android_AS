package com.fpliu.newton.framework.designpattern;

import java.util.ArrayList;
import java.util.List;

/**
 * 被观察者，根据java.util.Observable修改而来，支持泛型和可变参数
 * 
 * @author 792793182@qq.com 2015-03-03
 *
 */
public class Observable<T> {

	private List<Observer<T>> observers = new ArrayList<Observer<T>>();

	private boolean changed = false;

    /**
     * 添加观察者
     * @param observer 观察者
     */
    public void addObserver(Observer<T> observer) {
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
    public synchronized void deleteObserver(Observer<T> observer) {
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
     * @param data
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void notifyObservers(T... data) {
        int size = 0;
        Observer[] arrays = null;
        synchronized (this) {
            if (hasChanged()) {
                clearChanged();
                size = observers.size();
                arrays = new Observer[size];
                observers.toArray(arrays);
            }
        }
        if (arrays != null) {
            for (Observer<T> observer : arrays) {
                observer.update(this, data);
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
