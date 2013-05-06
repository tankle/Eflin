package com.elfin.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JComboBox;

import weibo4j.Friendships;
import weibo4j.model.Paging;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;
import weibo4j.util.Log;

import com.elfin.oauth.OAuth2Code;
import com.elfin.ui.OneUser.UserType;

public class FriendsOrFollowers extends WeiboPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5437734869654430571L;

	private static final String ALL_FRIENDS = "全部关注";
	private static final String BILATERAL = "互相关注";
	private static final String IN_COMMENT = "共同关注";
	private Friendships fm = new Friendships();
	private User user;
	private UserType type;
	private JComboBox comboBox;
	private String selectedItem;
	
	public FriendsOrFollowers(User user, UserType type) {
		super();
		this.user = user;
		this.type = type;
		
		initComboBox();
		
		init();
	}


	private void initComboBox() {
		selectedItem = ALL_FRIENDS;
		Vector<String> selectItem = new Vector<String>();
		selectItem.add(ALL_FRIENDS);
		if(user.equals(OAuth2Code.getUser()))
			selectItem.add(BILATERAL);
		else {
			selectItem.add(IN_COMMENT);
		}
		/**
		 * 我关注的
		 */
		if (type.equals(UserType.MY_FRIENDS) ||
				type.equals(UserType.OTHER_USER_FRIENDS)) {
			comboBox = new JComboBox(selectItem);
			comboBox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						removeAll();
						selectedItem = (String) comboBox.getSelectedItem();
						page = 1;
						gbc.gridy = 0;
						setLoading(true);
						addList();
					}
				}
			});
		}
	}


	@Override
	public void run() {
		setLoading(true);
		UserWapper userWapper = null;
		try {
			if (type.equals(UserType.MY_FRIENDS)) {// 是当前用户所关注的
				if (page == 1) {
					add(comboBox, gbc);
					++gbc.gridy;
					getParent().validate();// 调用validate调整布局
				}
				if (selectedItem.equals(ALL_FRIENDS))// 所有关注
					userWapper = fm.getFriendsByID(user.getId(), new Paging(
							page));
				else if (selectedItem.equals(BILATERAL)) {// 互相关注
					userWapper = fm.getFriendsBilateral(user.getId(), 0,
							new Paging(page));
				}
			} else if (type.equals(UserType.MY_FOLLOWERS)) {// 是当前用户的粉丝
				userWapper = fm
						.getFollowersById(user.getId(), new Paging(page));
			} else if (type.equals(UserType.OTHER_USER_FRIENDS)) {// 查看别的用户所关注的
				if (page == 1) {
					add(comboBox, gbc);
					++gbc.gridy;
					getParent().validate();// 调用validate调整布局
				}
				if (selectedItem.equals(ALL_FRIENDS))// 所有关注
					userWapper = fm.getFriendsByID(user.getId(), new Paging(
							page));
				else if (selectedItem.equals(IN_COMMENT)) {// 共同关注
					userWapper = fm.getFriendsInCommon(user.getId(), OAuth2Code
							.getUser().getId(), new Paging(page));
				}
			} else if (type.equals(UserType.OTHER_USER_FOLLOWERS)) {// 查看别的用户的粉丝
				userWapper = fm
						.getFollowersById(user.getId(), new Paging(page));
			}
		} catch (WeiboException e1) {
			e1.printStackTrace();
		}
		
		for (User u : userWapper.getUsers()) {
			if (selectedItem.equals(BILATERAL)) {
				u.setFollowing(true);// 修正错误了的API
				u.setFollowMe(true);
			}else if(selectedItem.equals(IN_COMMENT)){
				u.setFollowing(true);// 修正错误了的API
			}
			
			
			OneUser oneUser = null;
			try {
				oneUser = new OneUser(u);
			} catch (ClassCastException e) {
				Log.logInfo("ClassCastException FriendsOrFollowers 111");
			}
			if (oneUser == null) {
				continue;
			}
			add(oneUser, gbc);
			++gbc.gridy;
			getParent().validate();// 调用validate调整布局
		}
		++page;
		setLoading(false);
	}

}
