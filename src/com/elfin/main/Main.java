package com.elfin.main;


import javax.swing.SwingUtilities;

import weibo4j.Account;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;

import com.elfin.oauth.OAuth2Code;
import com.elfin.ui.LoginView;
import com.elfin.ui.MainDialog;

/**
 * 程序入口函数
 * 
 * @author JasonTan
 * E-mail: tankle120@gmail.com
 * Create on：2013-5-6 下午5:00:24 
 *
 */
public class Main {
	
	public static void main(String[] args) {
		
		new LoginView();
	}

}
