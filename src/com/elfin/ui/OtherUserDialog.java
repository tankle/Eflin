package com.elfin.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import weibo4j.model.User;

import com.elfin.oauth.OAuth2Code;
import com.elfin.ui.OneUser.UserType;

/**
 * 非登录用户的个人信息界面+微博界面
 * 
 * @author Jack_Tan
 *
 */
public class OtherUserDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6320855222348607926L;

	private JTabbedPane tabbedPane;
	private final static boolean[] arrayOfTab = { true, false, false };
	private User user;
	public OtherUserDialog(User user) {
		this.user = user;
		setTitle(user.getName());
		setIconImage(Toolkit.getDefaultToolkit().getImage(WeiboConstants.LOGO));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLayout(null);
		setSize(500, 610);
		
		getContentPane().setBackground(Color.white);
		
		//设置位置，靠近MainDialog
		setLocation(OAuth2Code.getMainDialog().getLocationOnScreen().x - OAuth2Code.getMainDialog().WIDTH,
				OAuth2Code.getMainDialog().getLocationOnScreen().y);
		
		setResizable(false);
		
		JPanel personMsg = new PersonalInform(user);
		personMsg.setBounds(0, 0, 485, 95);
		add(personMsg);
		
		initTabbedPanel();
		
		setVisible(true);
	}

	private void initTabbedPanel() {
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 11));
		add(tabbedPane);
		
		tabbedPane.addTab("微博", null, new MyScrollPane(new UserTimeline(user)),
				"微博");
		tabbedPane.addTab("关注", null, new MyScrollPane(), "关注");
		tabbedPane.addTab("粉丝", null, new MyScrollPane(), "粉丝");
		
		tabbedPane.setBounds(0, 96, 485, 475);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int index = tabbedPane.getSelectedIndex();// 用于判断是否是第一次点击
				if (arrayOfTab[index]) {
					return;
				}
				FirstClick thread = new FirstClick((MyScrollPane) tabbedPane
						.getSelectedComponent(), index);
				thread.start();
			}
		});

	}
	private class FirstClick extends Thread {
		private static final int FRIENDS = 1;
		private static final int FOLLOWERS = 2;
		private MyScrollPane msp;
		private int index;

		public FirstClick(MyScrollPane msp, int index) {
			this.msp = msp;
			this.index = index;
			arrayOfTab[index] = true;
		}

		@Override
		public void run() {
			if (index == FRIENDS) {// 关注
				msp.setViewportView(new FriendsOrFollowers(user,
						UserType.OTHER_USER_FRIENDS));
			} else if (index == FOLLOWERS) {// 粉丝
				msp.setViewportView(new FriendsOrFollowers(user,
						UserType.OTHER_USER_FOLLOWERS));
			}
		}
	}
}
