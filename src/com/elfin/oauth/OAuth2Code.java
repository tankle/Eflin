package com.elfin.oauth;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import weibo4j.Account;
import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.http.AccessToken;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;
import weibo4j.util.Log;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;

import com.elfin.ui.MainDialog;
import com.elfin.util.FileUtil;

/**
 * 
 * @author Jason Tan
 * E-mail: tankle120@gmail.com
 * Create on：2013-5-16
 *
 */
public class OAuth2Code {
	private final static String CODE = "?code=";
	private static MainDialog mainDialog;
	private Oauth oauth = new Oauth();
	private String lastURL;
	private static User user;// 当前登录的用户
	private JDialog dialog;

	public OAuth2Code() {
		Log.logInfo("正在打开授权的界面...");
		dialog = new JDialog() {
			private static final long serialVersionUID = -4925844767877475984L;
			protected void processWindowEvent(WindowEvent e) {
				super.processWindowEvent(e);
				if (e.getID() == WindowEvent.WINDOW_CLOSING) {
					dialog.dispose();
					System.exit(0);
				}
			}
		};
		dialog.setTitle("授权");
		dialog.setResizable(false);
		dialog.setBounds(30, 30, 608, 522);
		dialog.add(getWebBrowser(), BorderLayout.CENTER);
		dialog.setVisible(true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
		
		dialog.setLocation((dm.width/2) - 200, dm.height/2 - 200);
		
	}

	public OAuth2Code(String accessToken) throws WeiboException {
		init(accessToken);
	}

	/**
	 *  使用自行的 webBrowser
	 * @return
	 */
	private JWebBrowser getWebBrowser() {
		JWebBrowser webBrowser = new JWebBrowser();
		webBrowser.setBarsVisible(false);
		webBrowser.setButtonBarVisible(false);
		webBrowser.setDefaultPopupMenuRegistered(false);
		try {
			lastURL = oauth.authorize("code");
			webBrowser.navigate(lastURL);
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		webBrowser.addWebBrowserListener(new WebBrowserAdapter() {
			@Override
			public void locationChanged(WebBrowserNavigationEvent arg0) {
				String site = arg0.getWebBrowser().getResourceLocation();
				if (site == null || lastURL.equals(site)
						|| site.indexOf(CODE) == -1) {
					return;
				}
				lastURL = site;
				dialog.dispose();
				String code = site.substring(site.lastIndexOf("code=") + 5);
				String accessToken = getAccessTokenByCode(code);
				if (accessToken == null) {
					return;
				}
				FileUtil.write(accessToken);
				Log.logInfo("授权成功...");
				try {
					init(accessToken);
				} catch (WeiboException e) {
					e.printStackTrace();
				}
			}

		});
		return webBrowser;
	}
	
	private void init(String accessToken) throws WeiboException {
		Log.logInfo("正在打开主界面...");
		Weibo weibo = new Weibo();
		weibo.setToken(accessToken);
		Account ac = new Account();
		String uid = null;
		try {
			uid = ac.getUid().get("uid").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (uid == null) {
			return;
		}
		Users um = new Users();
		user = um.showUserById(uid);
//		if(null != mainDialog){
//			mainDialog.reStart(user);
//			
//		}
		mainDialog = new MainDialog(user);
	}

	private String getAccessTokenByCode(String code) {
		Log.logInfo("code:" + code);
		AccessToken accessToken = null;
		try {
			accessToken = oauth.getAccessTokenByCode(code);
		} catch (WeiboException e) {
			if (401 == e.getStatusCode()) {
				Log.logInfo("Unableto get the access token.");
			} else {
				e.printStackTrace();
			}
			return null;
		}
		return accessToken.getAccessToken();

	}

	public static MainDialog getMainDialog() {
		return mainDialog;
	}

	public static User getUser() {
		return user;
	}
}
