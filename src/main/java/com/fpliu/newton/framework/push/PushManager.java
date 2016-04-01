package com.fpliu.newton.framework.push;

import java.util.List;

import net.tsz.afinal.FinalDb;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.framework.designpattern.Observable;
import com.fpliu.newton.framework.designpattern.Observer;

/**
 * 推送推送管理类
 * 
 * @author 792793182@qq.com 2014-12-4
 * 
 */
public final class PushManager {

	private FinalDb db;

	private Observable<PushMessage> observable;

	private PushManager() {
		db = FinalDb.create(MyApp.getApp(), "pushMessage.db");
		observable = new Observable<PushMessage>();
	}

	private static final class InstanceHolder {
		private static PushManager instance = new PushManager();
	}

	public static PushManager getInstance() {
		return InstanceHolder.instance;
	}

	public int queryMessageNum() {
		List<PushMessage> pushMessages = db.findAll(PushMessage.class);
		return pushMessages.size();
	}

	public void save(PushMessage pushMessage) {
		if (pushMessage != null) {
			db.save(pushMessage);
			
			observable.setChanged();
			observable.notifyObservers(pushMessage);
		}
	}

	public void registerObserver(Observer<PushMessage> observer) {
		observable.addObserver(observer);
	}

	public void removeObserver(Observer<PushMessage> observer) {
		observable.deleteObserver(observer);
	}
}
