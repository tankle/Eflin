package com.elfin.ui;

import weibo4j.model.Paging;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

import com.elfin.ui.OneStatus.StatusType;

/**
 * 微博的“全部转发“
 * 
 * @author Jack_Tan
 *
 */
public class RepostTimeline extends StatusPanel {
	private static final long serialVersionUID = 1807129381526263701L;
	private String id;
	public RepostTimeline(String id) {
		super();
		this.id = id;
	}
	
	@Override
	protected StatusWapper getStatusWapper() {
		StatusWapper statusWapper = null;
		try {
			if(lastId == Long.MAX_VALUE){
				statusWapper = tm.getRepostTimeline(id, new Paging(1));
			}else{
				statusWapper = tm.getRepostTimeline(id, lastId);
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return statusWapper;
	}

//	@Override
//	protected void getType() {
//		type = StatusType.REPOST_TIMELINE;
//	}

}
