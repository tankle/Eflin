package com.elfin.ui;

import java.util.List;

import weibo4j.Timeline;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;

import com.elfin.ui.OneStatus.StatusType;

/**
 * 所有有关“微博”的父类
 * 
 * @author Jack_Tan
 *
 */
public abstract class StatusPanel extends WeiboPanel {

	private static final long serialVersionUID = 4618423940107947231L;
	protected static Timeline tm = new Timeline();
	protected StatusType type;
	public StatusPanel() {
		super();
	}
	
	@Override
	public void run() {
		loading = true;
//		showTip("正在获取数据...");
		List<Status> list = getStatusWapper().getStatuses();
		int statusCount = list.size();
		for (int i = 0; i < statusCount; ++i) {
			Status status = list.get(i);
			long id = Long.parseLong(status.getId());
			if(id == lastId){
				continue;
			}
			lastId = id;
			OneStatus oneStatus = new OneStatus(status, type);
			add(oneStatus, gbc);
			getParent().validate();
			gbc.gridy++;
//			showTip("正在载入\"" + status.getUser().getName() + "\"的微博("
//					+ (i+1) + "/" + statusCount + ")");
		}
//		tip.dispose();
		loading = false;
	}

	protected abstract StatusWapper getStatusWapper();
	

}
