package com.elfin.ui;

import weibo4j.model.Paging;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

import com.elfin.ui.OneStatus.StatusType;

/**
 * 某个用户所发的微博
 * 
 * @author Jack_Tan
 *
 */
public class UserTimeline extends StatusPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3742451241713875313L;

	private User user;
	public UserTimeline(User user) {
		super();
		type = StatusType.USER_TIMELINE;
		this.user = user;
		init();
	}
	
	@Override
	protected StatusWapper getStatusWapper() {
		StatusWapper statusWapper = null;
		try {
			if(lastId == Long.MAX_VALUE){
				statusWapper = tm.getUserTimelineByUid(user.getId(), new Paging(1), 0, 0);
			}else{
				statusWapper = tm.getUserTimelineByUid(lastId+"",new Long(0));
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return statusWapper;
	}

}
