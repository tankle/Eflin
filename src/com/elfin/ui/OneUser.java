package com.elfin.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import weibo4j.model.User;

public class OneUser extends OnePanel {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8760807057565893047L;

	public static final int CURRENT_USER_QUERY = 1;
	public static final int NO_CURRENT_USER_QUERY = 2;
	private static final String MUTUAL = "images/mutual.png";
	private static final Font FONT = new Font("微软雅黑", Font.PLAIN, 12);//默认字体
	private JLabel mutualLabel;// 互相关注
	private JPanel inner;
	private int panelWidth;
	
	/**
	 * StatusPanel的枚举StatusType
	 */
	protected enum UserType {
		MY_FOLLOWERS("粉丝"), 
		MY_FRIENDS("关注"), 
		OTHER_USER_FOLLOWERS("另一个用户的粉丝"), 
		OTHER_USER_FRIENDS("另一个用户所关注的");
		private String name;

		UserType(String name) {
			this.name = name;
		}

		public String getTypeName() {
			return name;
		}
	}

	public OneUser(User user) {
		
		init(user);

	}

	private void init(User user) {
		setLayout(new BorderLayout());
		inner = new JPanel();
		inner.setBackground(Color.white);
		inner.setLayout(null);
		
		/**
		 * 用户头像
		 */
		UserHeadImg userHead = new UserHeadImg(user);
		userHead.setBounds(locX, locY, headImgWidth, headImgHeight);
		inner.add(userHead);
		
		initFollowLabel(user);// 关注的标签
		initMutualLabel(user);// 互相关注的标签 
		
		int leftHeight = locY + headImgHeight + 5;
		initNameAndLocation(user);// 名字、地区
		initBasicMsg(user);// 粉丝、关注、微博
		int rightHeight = locY;
		//个人描述
		if (user.getDescription() != null) {
			rightHeight += initDescrip(user);
		}
		inner.setBorder(border);
		allHeight = rightHeight > leftHeight ? rightHeight : leftHeight;
		allHeight += 5;
		setPreferredSize(new Dimension(allWidth, allHeight));
		add(inner);
	}



	private int initDescrip(User user) {
		JTextPane textPanel = initTextPanel();
		textPanel.setFont(FONT);
		textPanel.setText("简介：" + user.getDescription());
		Dimension dm = textPanel.getPreferredSize();
		textPanel.setSize(allWidth - headImgWidth, (int) dm.getHeight());
		int textPanelHeight = (int) textPanel.getPreferredSize().getHeight();
		textPanel.setBounds(locX + headImgWidth, locY, allWidth - headImgWidth
				- 5, textPanelHeight);
		inner.add(textPanel);
		return textPanelHeight;
	}

	/**
	 * 
	 * @param user
	 */
	private void initBasicMsg(User user) {
		JTextPane textPanel = initTextPanel();
		textPanel.setFont(FONT);
		textPanel.setText(String.valueOf("关注:" + user.getFriendsCount())
				+ "   " + String.valueOf("粉丝:" + user.getFollowersCount())
				+ "   " + String.valueOf("微博:" + user.getStatusesCount()));
		textPanel.setLocation(locX + headImgWidth, locY);
		Dimension dm = textPanel.getPreferredSize();
		textPanel.setSize(dm);
		inner.add(textPanel);
		locY += dm.getHeight();		
	}

	/**
	 * 
	 * @param user
	 */
	private void initNameAndLocation(User user) {
		JTextPane textPanel = initTextPanel();
		textPanel.setFont(FONT);
		String text = "<a href='@" + user.getName() + "'>" + user.getName()
				+ "</a>";
		//备注消息
		String remark = user.getRemark();
		if (remark != null) {
			text += "(" + remark + ")<font size='3'>&nbsp&nbsp";
		} else {
			text += "<font size='3'>&nbsp&nbsp";
		}
		String gender = user.getGender();
		if (gender.equals("m")) {
			text += "男";
		} else if (gender.equals("f")) {
			text += "女";
		}
		text += "&nbsp&nbsp" + user.getLocation() + "</font>";
		textPanel.setText(text);
		textPanel.setLocation(locX + headImgWidth, locY);
		Dimension dm = textPanel.getPreferredSize();
		textPanel.setSize(dm);
		inner.add(textPanel);
		locY += dm.getHeight();
	}

	/**
	 * 
	 * @param user
	 */
	private void initFollowLabel(User user) {
		MyLabel follow = new MyLabel(MyLabel.FOLLOW, user);
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBackground(Color.white);
		panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		panel.add(follow);
		Dimension dm = panel.getPreferredSize();
		
		if (user.isFollowing()) {
			follow.setText(MyLabel.UNFOLLOW);
		} else {
			follow.setText(MyLabel.FOLLOW);
		}
		
		panelWidth = (int) dm.getWidth();
		int labelLocX = (int) (allWidth - panelWidth) - 5;
		panel.setSize(dm);
		panel.setLocation(labelLocX, locY);
		inner.add(panel);
	}

	/**
	 * 
	 * @param user
	 */
	private void initMutualLabel(User user) {
		mutualLabel = new JLabel(new ImageIcon(MUTUAL));
		mutualLabel.setToolTipText("互相关注");
		Dimension dm = mutualLabel.getPreferredSize();
		int labelWidth = (int) dm.getWidth();
		int labelLocX = (int) (allWidth - panelWidth - labelWidth - 5);
		mutualLabel.setLocation(labelLocX, locY + 5);
		mutualLabel.setSize(dm);
		mutualLabel.setVisible(false);
		inner.add(mutualLabel);
		if (user.isFollowing() && user.isfollowMe()) {
			mutualLabel.setVisible(true);
		}
	}
	

	public boolean isMutualVisible() {
		return mutualLabel.isVisible();
	}

	public void setMutualVisible(boolean flag) {
		mutualLabel.setVisible(flag);
	}
}
