package syamwu.xchushi.fw.common.observer;

import java.util.ArrayList;
import java.util.List;

import syamwu.xchushi.fw.common.Asset;

/**
 * 抽象主题类
 * 
 * @author: syam_wu
 * @date: 2018
 */
public abstract class AbstractSubject<T> implements Subject<T> {

    /**
     * 用来保存注册的观察者对象
     */
    private List<Observer<T>> list = new ArrayList<Observer<T>>();

    public void attach(Observer<T> observer) {
        list.add(observer);
    }
    
    public void attach(Observer<T>[] observers) {
        Asset.notNull(observers);
        for (Observer<T> observer : observers) {
            attach(observer);
        }
    }

    public void detach(Observer<T> observer) {
        list.remove(observer);
    }
    
    public void detach(Observer<T>[] observers) {
        Asset.notNull(observers);
        for (Observer<T> observer : observers) {
            detach(observer);
        }
    }

    public void nodifyObservers(T changeData) {
        for (Observer<T> observer : list) {
            observer.notify(changeData);
        }
    }

}
