package com.fpliu.newton.framework.designpattern;

/**
 * 观察者，根据java.util.Observer修改而来，支持泛型和可变参数
 * 
 * @author 792793182@qq.com 2015-03-03
 *
 */
public interface Observer<T> {

    /**
     * 更新数据
     * @param observable  被观察者
     * @param data        数据
     */
    void update(Observable<T> observable, T... data);
}
