package com.elfin.ui;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import weibo4j.Comments;
import weibo4j.Favorite;
import weibo4j.Friendships;
import weibo4j.Timeline;
import weibo4j.model.Comment;
import weibo4j.model.Status;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

import com.elfin.oauth.OAuth2Code;


/**
 * 重载JLabel的一个类，方便用于微博客户端的实现
 * 
 * @author Jack_Tan
 *
 */
public class MyLabel extends JLabel implements MouseListener{
	private static final long serialVersionUID = -4476206131490069863L;
	private static final String fontName = "微软雅黑";// 默认的字体
	public static final String REPOST = "转发";
	public static final String COMMENT = "评论";
	public static final String ORIGINAL_REPOST = "原文转发";
	public static final String ORIGINAL_COMMENT = "原文评论";
	public static final String FAVORITE = "收藏";
	public static final String UNFAVORITE = "取消收藏";
	public static final String DELETE = "删除";
	public static final String All_COMMENTS = "全部评论";
	public static final String REPLY = "回复";
	public static final String REFRESH = "刷新 ";
	public static final String UPDATE_STATUS = "发微博";
	public static final String FOLLOW = "+关注";
	public static final String UNFOLLOW = "取消关注";
	public static final String DIRECT_MESSAGES = "私信";
	
	private int size = 14;// /字体的大小，默认为14。
	private boolean isEntered;
	
	private String type;
	private Status status;
	private Comment comment;
	private User user;
	
	
	public MyLabel(String type) {
		super(type);
		this.type = type;
		init();
	}
	
	/**
	 * 
	 * @param text
	 * @param size
	 *            字体大小
	 */
	public MyLabel(String text, int size) {
		super(text);
		this.type = text;
		init();
	}

	/**
	 * 
	 * @param text
	 * @param status
	 */
	public MyLabel(String text, Status status) {
		super(text);
		this.type = text;
		this.status = status;
		init();
	}
	/**
	 * 
	 * @param text
	 * @param comment
	 */
	public MyLabel(String text, Comment comment) {
		super(text);
		this.type = text;
		this.comment = comment;
		init();
	}
	
	/**
	 * 
	 * @param text
	 * @param user
	 */
	public MyLabel(String text, User user) {
		super(text);
		this.type = text;
		this.user = user;
		init();
	}

	/**
	 * 
	 */
	private void init() {
		setFont(new Font(fontName, Font.PLAIN, size));
		setSize(getPreferredSize());
		addMouseListener(this);
	}

	protected void paintBorder(Graphics g) {
		int w = this.getSize().width;
		int h = this.getSize().height;
		if (isEntered) {
			g.drawLine(0, h - 1, w - 1, h - 1);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		new Thread() {
			@Override
			public void run() {
				//评论和转发
				if (type.startsWith(REPOST) || type.startsWith(COMMENT)
						|| type.startsWith(ORIGINAL_REPOST)
						|| type.startsWith(ORIGINAL_COMMENT)) {
					WeiboFrame.getInstance(status, type);
				} else if (type.equals(DELETE)) {
					delete();//删除
				} else if(type.equals(REFRESH)){
					refresh();//刷新
				} else if(type.equals(REPLY)){
					reply();//回复
				} else if(type.equals(UPDATE_STATUS)){
					UpdateStatus.getInstance();//发微博
				}else if(type.equals(FAVORITE)){
					createFavorite();//收藏
				}else if(type.equals(UNFAVORITE)){
					unFavorite();//取消收藏
				}else if(type.equals(FOLLOW)){
					follow();//关注
				}else if(type.equals(UNFOLLOW)){
					unFollow();//取消关注
				}
			}

		}.start();		
	}
	
	/**
	 * 
	 */
	protected void unFollow() {
		int result = JOptionPane.showConfirmDialog(null, "确定要取消关注"
				+ user.getName() + "？", "取消关注？", JOptionPane.OK_CANCEL_OPTION);
		if (result != JOptionPane.OK_OPTION) {
			return;
		}
		Friendships fm = new Friendships();
		User fv = null;
		try {
			fv = fm.destroyFriendshipsDestroyById(user.getId());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		if (fv != null) {
			JOptionPane.showMessageDialog(null, "已取消关注！");
			setText(MyLabel.FOLLOW);
			paintBorder(getGraphics());
			OneUser oneUser = (OneUser) getParent().getParent().getParent();
			if (oneUser.isMutualVisible()) {
				oneUser.setMutualVisible(false);
			}
		} else {
			JOptionPane.showConfirmDialog(null, "操作失败，请稍后再试。", "错误",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 
	 */
	protected void follow() {
		Friendships fm = new Friendships();
		User fv = null;
		try {
			fv = fm.createFriendshipsById(user.getId());
		} catch (WeiboException e) {
			if(e.getErrorCode() == 20506){
				JOptionPane.showConfirmDialog(null, "已经关注此用户。", "错误",
						JOptionPane.ERROR_MESSAGE);
			}
			e.printStackTrace();
		}
		if (fv != null) {
			JOptionPane.showMessageDialog(null, "成功关注！");
			setText(UNFOLLOW);
			paintBorder(getGraphics());
			if (user.isfollowMe()) {// 这里不需要user.isFollowing() && user.isfollowMe()
				OneUser oneUser = (OneUser) getParent().getParent().getParent();
				oneUser.setMutualVisible(true);
			}
		} else {
			JOptionPane.showMessageDialog(null, "操作失败，请稍后再试。", "错误",
					JOptionPane.ERROR_MESSAGE);
		}		
	}

	/**
	 *取消收藏 
	 */
	protected void unFavorite() {
		int result = JOptionPane.showConfirmDialog(null, "确定要取消这条收藏？", "删除？",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			boolean isSuccessed = false;
			Favorite fv = new Favorite();
			try {
				isSuccessed = (fv.destroyFavorites(status.getId()) != null) ? true
						: false;
			} catch (WeiboException e) {
				e.printStackTrace();
			}
			if (isSuccessed) {
				JOptionPane.showMessageDialog(null, "操作成功！");
				setText(FAVORITE);
			} else {
				JOptionPane.showMessageDialog(null, "操作失败！请稍后再试", "错误",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * 收藏某条微博
	 */
	protected void createFavorite() {
		boolean isSuccessed = false;
		Favorite fv = new Favorite();
		try {
			isSuccessed = (fv.createFavorites(status.getId()) != null) ? true
					: false;
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		if (isSuccessed) {
			JOptionPane.showMessageDialog(null, "操作成功！");
			setText(UNFAVORITE);// 变为“取消收藏”
		} else {
			JOptionPane.showMessageDialog(null, "操作失败！请稍后再试", "错误",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void reply() {
		WeiboFrame weiboFrame = WeiboFrame.getInstance();
		JTextArea textArea = weiboFrame.
			getTextArea(MyLabel.REPLY);
		weiboFrame.setReplyComment(comment);
		String text = "回复@" + comment.getUser().getName() + ":";
		textArea.setText(text);
		textArea.setCaretPosition(text.length());	
	}

	protected void refresh() {
		MyScrollPane jsp = (MyScrollPane)OAuth2Code.getMainDialog().
		getTabbedPane().getSelectedComponent();
		WeiboPanel weiboPanel = (WeiboPanel)jsp.getViewport().getView();
		if(weiboPanel != null){
			weiboPanel.refresh();
		}		
	}

	private void delete() {
		String text = null;
		if(status != null){
			text = "微博";
		}else if(comment != null){
			text = "评论";
		}
		int result = JOptionPane.showConfirmDialog(null,
				"确定要删除这条" + text + "？", "删除？",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			boolean isSuccessed = false;
			if(status != null){
				isSuccessed = destroyStatus(status); 
			}else if(comment != null){
				isSuccessed = destroyComment(comment); 
			}
			if(isSuccessed){
				JOptionPane.showMessageDialog(null,
					"操作成功！");
				
			}else {
				JOptionPane.showMessageDialog(null,
						"操作失败！请稍后再试", "错误",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private boolean destroyComment(Comment comment2) {
		Comments cm = new Comments();
		//cid 要删除的评论ID，只能删除登录用户自己发布的评论
		Comment result = null;
		try {
			result = cm.destroyComment(comment.getIdstr());
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result != null;
	}

	private boolean destroyStatus(Status status2) {
		Timeline tl = new Timeline();
		Status result = null;
		try {
			result = tl.Destroy(status.getId());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return result != null;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		isEntered = false;
		repaint();		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		isEntered = true;
		repaint();
		setCursor(new Cursor(Cursor.HAND_CURSOR));		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		isEntered = false;
		repaint();		
	}

	
//	private static final long serialVersionUID = -4476206131490069863L;
//	private static final String fontName = "微软雅黑";//默认的字体
//	private int size = 14;///字体的大小，默认为14。
//	public MyLabel(String text){
//		super(text);
//		init();
//	}
//	/**
//	 * 
//	 * @param text
//	 * @param size 字体大小
//	 */
//	public MyLabel(String text, int size){
//		super(text);
//		init();
//	}
//	
//	private void init(){
//		setFont(new Font(fontName, Font.PLAIN, size));
//		setSize(getPreferredSize());
//	}
}