package com.elfin.ui;


import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import weibo4j.model.User;

import com.elfin.oauth.OAuth2Code;

/**
 * 用于放置个人信息的JPane
 * 
 * @author Jason Tan
 * E-mail: tankle120@gmail.com
 * Create on：2013-5-17
 *
 */
public class PersonalInform extends JPanel{
	
	private static final long serialVersionUID = -9114776723837074171L;
	private User user;
	private Thread initThread = null;
	
	public PersonalInform(User user){
		this.user = user;
		setLayout(null);
		
		initThread = new Thread(){
			@Override
			public void run() {
				init();
			}
			
		};
		
		initThread.start();
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
//	
//	public void refreshInfo(User user){
//		setUser(user);
//		
////		if(initThread.isAlive()){
////			initThread.start();
////		}
//		initThread.start();
//	}
//	
	/**
	 * 启动一个新的线程来更新消息
	 */
	private void init() {
		//头像
		UserHeadImg userHead = new UserHeadImg(user);
		userHead.setLocation(8, 8);
		userHead.setHorizontalAlignment(SwingConstants.LEFT);//设置标签内容沿 X轴的对齐方式。
		add(userHead);
		
		//用户名字,使用自定义的显示方式
		JLabel nameLabel = new MyLabel(user.getName());
		nameLabel.setSize(nameLabel.getPreferredSize());
		nameLabel.setLocation(70, 8);
		add(nameLabel);
		
		//地区
		JLabel locationLabel = new MyLabel(user.getLocation(), 12);
		locationLabel.setLocation(72, 30);
		add(locationLabel);
		
		//性别
		String gender = getGender(user.getGender());
		JLabel userGender = new MyLabel(gender);
		userGender.setLocation(165, 30);
		add(userGender);
		
		int locX = 8;//控制三个标签的位置（关注，粉丝，微博）
		JLabel friendsLabel = new MyLabel(
				"关注 "+Integer.toString(user.getFriendsCount()), 10);
		friendsLabel.setLocation(locX, 70);
		add(friendsLabel);
		

		locX += friendsLabel.getPreferredSize().getWidth()+25;
		JLabel followersLabel = new MyLabel(
				"粉丝 "+Integer.toString(user.getFollowersCount()), 10);
		followersLabel.setLocation(locX, 70);
		add(followersLabel);
		

		locX += followersLabel.getPreferredSize().getWidth()+25;
		JLabel statusLabel = new MyLabel(
				"微博 "+String.valueOf(user.getStatusesCount()), 10);
		statusLabel.setLocation(locX, 70);
		add(statusLabel);
		
		
//		JLabel refreshLabel = new JLabel("刷新 ");
//		refreshLabel.setLocation(390, 70);
//		add(refreshLabel);
		if(user.equals(OAuth2Code.getUser())) {
			JLabel refreshLabel = new MyLabel(MyLabel.REFRESH);//刷新
			refreshLabel.setLocation(440, 70);
			add(refreshLabel);
			validate();
			
			JLabel update = new MyLabel(MyLabel.UPDATE_STATUS);//发微博
			update.setLocation(370, 70);
			add(update);
			validate();
		}
		
		repaint();
	}
	/**
	 * 
	 * @param gender
	 * @return 男/女/
	 */
	private String getGender(String gender) {
		String temp = "";
		if (gender.equals("f")) {
			temp = "女 ";
		} else if (gender.equals("m")) {
			temp = "男 ";
		}
		return temp;
	}
}
