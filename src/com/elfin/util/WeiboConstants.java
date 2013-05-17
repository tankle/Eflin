package com.elfin.util;

import java.awt.Font;
import java.awt.Toolkit;

/**
 * 存放一些用到的常量
 * @author Jack_Tan
 *
 */
public class WeiboConstants {
	/**
	 * 
	 */
	public final static String LOGO = "images/logo.png";
	public static final Font DEFAULT_FONT = new Font("微软雅黑", Font.PLAIN, 14);//默认字体
	
	public  final static String OPEN = "Open";
	public final static String EXIT = "Exit";
	public final static String ABOUT = "About";
	public final static String LOGOUT = "Logout";
	
	/**
	 * 保存用户的token文件
	 */
	public final static String USER_TOKEN_FILE = "data/users.wb";
	/**
	 *  主界面标题
	 */
	public static final String MAIN_TITLE = "微博桌面小精灵-Elfin";
}
