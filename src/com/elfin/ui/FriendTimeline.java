package com.elfin.ui;

import weibo4j.model.Paging;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

import com.elfin.ui.OneStatus.StatusType;

/**
 * 用于放置当前登录用户及其所关注用户的最新微博
 * 
 * @author Jack_Tan
 *
 */
public class FriendTimeline extends StatusPanel {

	private static final long serialVersionUID = 4618423940107947231L;
	public FriendTimeline() {
		super();
		type = StatusType.FRIEND_TIMELINE;
		init();	//调用父类的init，进行初始化
	}
	
	
	@Override
	protected StatusWapper getStatusWapper() {
		StatusWapper statusWapper = null;
		try {
			if(lastId == Long.MAX_VALUE){
				statusWapper = tm.getFriendsTimeline(0, 0,
						new Paging(1));
			}else{
				statusWapper = tm.getFriendsTimeline(0, 0, lastId);
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return statusWapper;
	}
//	@Override
//	protected void getType() {
//		type = StatusType.FRIEND_TIMELINE;		
//	}
	
}
