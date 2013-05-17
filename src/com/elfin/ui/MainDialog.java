package com.elfin.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import weibo4j.model.User;

import com.elfin.main.Main;
import com.elfin.oauth.OAuth2Code;
import com.elfin.ui.OneUser.UserType;
import com.elfin.util.FileUtil;
import com.elfin.util.WeiboConstants;

/**
 * 程序主界面
 * 增加了系统托盘功能实现
 * @author Jack_Tan
 *
 */
public class MainDialog extends JDialog{
	private static final long serialVersionUID = 3367550336433904925L;
	public static final int WIDTH = 480;
	public static final int HEIGHT = 610;
	
	private final static boolean[] arrayOfTab = { true, false, false, false,
		false, false, false};
	private JTabbedPane tabbedPane;
	private PersonalInform personMsg = null;


	public MainDialog(User user) {
		//设定界面的展示效果，设为系统的默认效果
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setTitle(WeiboConstants.MAIN_TITLE);
		
		//显示个人信息，主界面上面部分
		//setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(WeiboConstants.LOGO));
	//	setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setLayout(null);
		setSize(WIDTH, HEIGHT);
		setAlwaysOnTop(false);
		setResizable(false);
		getContentPane().setBackground(Color.WHITE);
		
		Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dm.width - WIDTH - 100, dm.height - HEIGHT - 80);
		
		personMsg = new PersonalInform(user);
		personMsg.setBounds(0,0,485,95);
		add(personMsg);
		
		//主界面中间部分
		initTabbedPanel();
		
		setVisible(true);

		initSystemTray();// 系统拖盘
		
		
	}
//
//	public void reStart(User user) {
//		personMsg.refreshInfo(user);
//	}
//	
	@SuppressWarnings("deprecation")
	@Override
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			//目前是直接系统托盘，后序会实现由用户选择
			this.hide();
//			this.dispose();
//			System.exit(0);
		}
	}

	private void initTabbedPanel() {
//		tabbedPane = 
//			new JTabbedPane(JTabbedPane.TOP);
//		tabbedPane.setFont(new Font("微软雅黑", 
//				Font.PLAIN, 11));
//		add(tabbedPane);
//		tabbedPane.addTab("首页", null, 
//				new MyScrollPane(new FriendTimeline()), "首页");
//		tabbedPane.addTab("评论", null,
//				new MyScrollPane(new MyComment()),"评论");
//	
//		tabbedPane.setBounds(0,96,485,475);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 11));
		add(tabbedPane);
		tabbedPane.addTab("首页", null, new MyScrollPane(new FriendTimeline()),
				"首页");
		tabbedPane.addTab("评论", null, new MyScrollPane(), "评论");
		tabbedPane.addTab("@我", null, new MyScrollPane(), "@我");
		tabbedPane.addTab("收藏", null, new MyScrollPane(), "收藏");
		tabbedPane.addTab("关注", null, new MyScrollPane(), "关注");
		tabbedPane.addTab("粉丝", null, new MyScrollPane(), "粉丝");
		tabbedPane.addTab("微博", null, new MyScrollPane(), "微博");
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
	
	/**
	 * 系统托盘
	 */
	private void initSystemTray() {
		if (!SystemTray.isSupported()) // 判断当前系统是否支持系统栏
			return;
		try {
			String title = "新浪微博JAVA客户端 \n桌面小精灵";// 设置提示文本信息
			SystemTray sysTray = SystemTray.getSystemTray();
			Image image = Toolkit.getDefaultToolkit().getImage(
					WeiboConstants.LOGO);
			TrayIcon trayicon = new TrayIcon(image, title, createMenu());// 创建托盘图标：由图标、文本、右击菜单组成
			trayicon.setImageAutoSize(true);// 设置是否自动调整图标的大小
			trayicon.addActionListener(new ActionListener()// 双击图标时显示窗体
					{
						public void actionPerformed(ActionEvent e) {
							setVisible(true);
							toFront();// 如果此窗口是可见的，则将此窗口置于前端，并可以将其设为焦点 Window。
						}
					});
			sysTray.add(trayicon);
			trayicon.displayMessage("新浪微博JAVA客户端", "桌面小精灵", MessageType.INFO);// 在托盘图标附近显示弹出消息。
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 右键菜单 还要改进
	 */
	private PopupMenu createMenu() { // 创建系统栏菜单的方法
		PopupMenu menu = new PopupMenu();
		/**
		 * 
		 */
		MenuItem openItem = new MenuItem(WeiboConstants.OPEN);
		openItem.addActionListener(new ActionListener() {// 系统栏打开菜单项事件
					public void actionPerformed(ActionEvent e) {
						if (!isVisible()) {
							setVisible(true);
							toFront();
						} else
							toFront();
					}
				});
		/**
		 * 退出选项
		 */
		MenuItem logoffItem = new MenuItem(WeiboConstants.LOGOUT);
		logoffItem.addActionListener(new ActionListener() {// 系统栏注销菜单项事件
					public void actionPerformed(ActionEvent e) {
						JOptionPane
						.showMessageDialog(null, "还没添加注销的功能！");
//						FileUtil.deleteToken();
//						OAuth2Code.getMainDialog().setVisible(false);
////						new LoginView();
//						
//						Main.login.start();
						
					}
				});
		/**
		 * 关于软件选项
		 */
		MenuItem aboutItem = new MenuItem(WeiboConstants.ABOUT);
		aboutItem.addActionListener(new ActionListener() {// 系统栏注销菜单项事件
					public void actionPerformed(ActionEvent e) {
						JOptionPane
								.showMessageDialog(null, "新浪微博 @桌面小精灵\n"
										+ "博客：http://blog.sina.com.cn/u/1746778205\n"
										+ "All Rights Reserved");
					}
				});
		/**
		 * 退出选项
		 */
		MenuItem exitItem = new MenuItem(WeiboConstants.EXIT);
		exitItem.addActionListener(new ActionListener() { // 系统栏退出事件
					public void actionPerformed(ActionEvent e) {
						dispose();
						System.exit(0);
					}
				});

		menu.add(openItem);
		menu.addSeparator();// 分割线
		menu.add(logoffItem);
		menu.addSeparator();// 分割线
		menu.add(aboutItem);
		menu.addSeparator();// 分割线
		menu.add(exitItem);

		return menu;
	}
	
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}
	
	/**
	 * 第一次点击主界面上的按钮
	 *
	 */
	private class FirstClick extends Thread {
		private static final int MYCOMMENT = 1;
		private static final int METIONS = 2;
		private static final int FAVORITES = 3;
		private static final int FRIENDS = 4;
		private static final int FOLLOWERS = 5;
		private static final int MYSTATUS = 6;
		private MyScrollPane msp;
		private int index;

		public FirstClick(MyScrollPane msp, int index) {
			this.msp = msp;
			this.index = index;
			arrayOfTab[index] = true;
		}

		@Override
		public void run() {
			if (index == MYCOMMENT) {// 评论
				msp.setViewportView(new MyComment());
			} else if (index == METIONS) {// @我
				msp.setViewportView(new Mentions());
			} else if (index == FAVORITES) {// 收藏
				msp.setViewportView(new MyFavorites());
			} else if (index == FRIENDS) {// 关注
				msp.setViewportView(new FriendsOrFollowers(OAuth2Code.getUser(),
						UserType.MY_FRIENDS));
			} else if (index == FOLLOWERS) {// 粉丝
				msp.setViewportView(new FriendsOrFollowers(OAuth2Code.getUser(),
						UserType.MY_FOLLOWERS));
			} else if (index == MYSTATUS) {// 微博
				msp.setViewportView(new UserTimeline(OAuth2Code.getUser()));
			}

		}

	}



}
