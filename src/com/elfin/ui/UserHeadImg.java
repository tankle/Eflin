package com.elfin.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import weibo4j.model.User;
import weibo4j.util.BareBonesBrowserLaunch;

/**
 * 用于放置用户头像JLabel
 * 
 * @author Jack_Tan
 *
 */
public class UserHeadImg extends JLabel{
	private static final long serialVersionUID = -856655269996837618L;
	public UserHeadImg(final User user){
		//获取用户的头像
		ImageIcon userImg = new ImageIcon(user.getProfileImageURL());
		setIcon(userImg);
		
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setSize(getPreferredSize());
		
		//添加鼠标滑过头像是的动作
		addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e) {
				setBorder(BorderFactory.createLineBorder(Color.BLUE));
			}
			public void mouseExited(MouseEvent e) {
				setBorder(null);
			}
			//跳转到用户的微博首页
			public void mouseClicked(MouseEvent e) {
				String url = "http://weibo.com/"+user.getId();
				 BareBonesBrowserLaunch.openURL(url);
			}
		});
	}
}
