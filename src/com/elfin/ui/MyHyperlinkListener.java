package com.elfin.ui;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import weibo4j.Users;
import weibo4j.model.WeiboException;
import weibo4j.util.BareBonesBrowserLaunch;

/**
 * 处理微博客户端中的超链接等
 * 
 * @author Jack_Tan
 *
 */
public class MyHyperlinkListener implements HyperlinkListener {

	public MyHyperlinkListener() {
	}
	
	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			String descrip = e.getDescription();
			System.out.println("getDescription(): " + descrip);
			if ("".equals(descrip)) {
				return;
			} 
			if (descrip.substring(0, 1).equals("*")) {//点击微博所含有的图片
				new ShowImage(descrip.substring(1, descrip.length()));
			} else if (descrip.substring(0, 1).equals("@")) {	//@了某个用户
				showUser(descrip);
			}  else if (descrip.substring(0, 7).equals("http://")	//微博中的超链接
					|| descrip.startsWith("https://")) {
				aboutHttp(descrip);
			}else if(descrip.substring(0,1).equals("#")){	//微博中的话题
				BareBonesBrowserLaunch.openURL("http://huati.weibo.com/k/"
						+ descrip.substring(1));
			}
		}	
	}


	private void aboutHttp(String descrip) {
		String lastOfUrl = descrip.substring(descrip.lastIndexOf(".") + 1);
		if ("|gif|jpg|jpeg|png|bmp|".indexOf("|" + lastOfUrl
				+ "|") > -1) {
			new ShowImage(descrip);
		} else {
			BareBonesBrowserLaunch.openURL(descrip);
		}
		
	}
	
	private void showUser(String descrip) {
		String username = descrip.substring(1, descrip.length());
		Users um = new Users();
		try {
			new OtherUserDialog(um.showUserByScreenName(username));
		} catch (WeiboException e1) {
			if(e1.getErrorCode()==20003){
				System.out.println("用户不存在！");
			}
		}
		
	}
}
