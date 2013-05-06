package com.elfin.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import weibo4j.Comments;
import weibo4j.Timeline;
import weibo4j.model.Comment;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;
import weibo4j.util.Log;

import com.elfin.main.Main;
import com.elfin.oauth.OAuth2Code;
import com.elfin.ui.OneStatus.StatusType;

/**
 * 转发、评论等
 * 
 * @author Jack_Tan
 *
 */
public class WeiboFrame extends JFrame {
	private static final long serialVersionUID = -2834918407575759328L;
	private static Status status;
	private static String type;
	private int is_comment;
	private JCheckBox firstCB;
	private static JTextArea repostTextArea;
	private static JTextArea commentTextArea;
	private static JTextArea textArea;
	private static JTabbedPane downTabbedPane;
	private Comment replyComment;
	private static WeiboFrame instance = null;
	
	
	public static WeiboFrame getInstance() {
		return instance;
	}
	
	public static WeiboFrame getInstance(Status status,
			String type) {	
		if (instance == null){
			instance = new WeiboFrame(status, type);
		} else {
			if(!status.equals(WeiboFrame.status)){
				instance.dispose();
				instance = new WeiboFrame(status, type);
			}else if(!type.equals(WeiboFrame.type)){
				SetSelectedIndex(type);
			}
		}
		return instance;
	}
	
	private WeiboFrame(Status status, String type) {
		super("新浪微博");
		WeiboFrame.type = type;
		WeiboFrame.status = status;
		getContentPane().setBackground(Color.white);
		setSize(500, 500);
		setLayout(null);
		//与MainDialog 靠近
		setLocationRelativeTo(OAuth2Code.getMainDialog());
		initUp(status);
		initDown();
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void initUp(final Status status) {
		final String id = status.getId();
		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 485, 280);
		tabbedPane.addTab("微博正文", null, getWeiboText(status), "微博正文");
		int repostsCount = status.getRepostsCount();
		int commentsCount = status.getCommentsCount();
		tabbedPane.addTab("全部转发" + descriCount(repostsCount), null,
				getRepostTimeline(id), "全部转发");
		tabbedPane.addTab("全部评论" + descriCount(commentsCount), null,
				getCommentTimeline(id), "全部评论");
		add(tabbedPane);
	}
	
	private void initDown() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		downTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		repostTextArea = initTextArea();
		JScrollPane jsp = new JScrollPane(repostTextArea);
		downTabbedPane.addTab("转发", null, jsp, "转发");
		commentTextArea = initTextArea();
		jsp = new JScrollPane(commentTextArea);
		downTabbedPane.addTab("评论", null, jsp, "评论");
		SetSelectedIndex(WeiboFrame.type);
		downTabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int index = downTabbedPane.getSelectedIndex();
				if (index == 0) {
					firstCB.setText("同时评论");
					textArea = repostTextArea;
				} else if (index == 1) {
					firstCB.setText("同时转发");
					textArea = commentTextArea;
				}
			}

		});
		panel.add(downTabbedPane);
		panel.add(getButtonPanel(), BorderLayout.SOUTH);
		panel.setBounds(0, 280, 485, 180);
		add(panel);
	}


	private Component getCommentTimeline(String id) {
		CommentById commentById = new CommentById(id);
		JScrollPane jsp = new MyScrollPane(commentById);
		return jsp;
	}

	private Component getWeiboText(Status status) {
		JScrollPane jsp = new JScrollPane(new OneStatus(status,
				StatusType.STATUS_TEXT));
		jsp.setBounds(10, 0, 480, 450);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.getVerticalScrollBar().setUnitIncrement(25);
		return jsp;
	}

	private Component getRepostTimeline(String id) {
		RepostTimeline reposts = new RepostTimeline(id);
		JScrollPane jsp = new MyScrollPane(reposts);
		return jsp;
	}

	private static void SetSelectedIndex(String type) {
		if (type.startsWith(MyLabel.ORIGINAL_REPOST)
				|| type.startsWith(MyLabel.REPOST)) {
			textArea = repostTextArea;
			downTabbedPane.setSelectedIndex(0);
		} else {
			textArea = commentTextArea;
			downTabbedPane.setSelectedIndex(1);
		}
		
	}
	private JTextArea initTextArea() {
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		return textArea;
	}

	private JPanel getButtonPanel() {
		JPanel btPanel = new JPanel();
		btPanel.setBackground(Color.white);
		StringBuffer cbText = new StringBuffer("同时");
		if (type.startsWith(MyLabel.ORIGINAL_REPOST)
				|| type.startsWith(MyLabel.REPOST)) {
			cbText.append("评论");
		} else {
			cbText.append("转发");
		}
		firstCB = new JCheckBox(cbText.toString());// +
		// status.getUser().getName()
		firstCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					is_comment += 1;
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					is_comment -= 1;
				}
			}
		});
		btPanel.add(firstCB);
		Status retweeted = status.getRetweetedStatus();
		if (retweeted != null) {
			JCheckBox secendCB = new JCheckBox("同时评论给原文作者");
			secendCB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						is_comment += 2;
					} else if (e.getStateChange() == ItemEvent.DESELECTED) {
						is_comment -= 2;
					}
				}
			});
			btPanel.add(secendCB);
		}
		JButton button = new JButton("发送");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Clicked().start();
			}
		});
		btPanel.add(button);
		return btPanel;
	}

	private String descriCount(int count) {
		if (count != 0) {
			return "(" + count + ")";
		}
		return "";
	}

	
	public void showOptionPane(int isSuccessed){
		if (isSuccessed == 1) {
			JOptionPane.showMessageDialog(null, "操作成功！");
			textArea.setText("");
		} else if (isSuccessed == -1) {
			JOptionPane.showMessageDialog(null, "操作失败，请稍后再试！", "错误",
					JOptionPane.ERROR_MESSAGE);
		} else if (isSuccessed == 20019) {
			JOptionPane.showMessageDialog(null, "不要太贪心哦，发一次就够啦", "错误",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	private int aboutComment(String statusId, String reason, boolean isRetweeted){
		int isSuccess = 0;
		if (replyComment != null){//是回复评论
			isSuccess = reply();
		}else {
			if (is_comment == 0) {// 只是评论当前微博
				isSuccess = createComment(statusId, reason, 0);
			} else if (is_comment == 2) {// 评论当前微博和原微博，但是不转发
				isSuccess = createComment(statusId, reason, 1);
			} else {
				// （is_comment == 1）转发并评论当前微博，但是不评论原微博
				// （is_comment == 3）转发并评论当前微博，并评论原微博
				if (isRetweeted) {// TODO 这里有问题，想想, 看看评论的理由就知道了。。。⊙﹏⊙汗
					reason += "//@" + status.getUser().getName() + ":"
							+ status.getText();
				}
				isSuccess = repost(statusId, reason, is_comment);
			}
		}
		return isSuccess;
	}
	
	private int repost(String id, String text, Integer is_comment) {
		Timeline tl = new Timeline();
		Status result = null;
		try {
			result = tl.Repost(id, text, is_comment);
		} catch (WeiboException e) {
			if (e.getErrorCode() == 20019) {
				return 20019;
			}
			e.printStackTrace();
			return -1;
		}
		if (result != null) {
			return 1;
		}
		return -1;
	}
	
	/**
	 * 
	 * @param id
	 * @param text
	 * @param comment_ori
	 * @return
	 */
	private int createComment(String id, String text, Integer comment_ori) {
		Comments cm = new Comments();
		Comment result = null;
		
		if(null == text){
			Log.logInfo("评论内容不能为空");
			return -1;
		}
		if(text.length() > 140){
			Log.logInfo("长度不能超过140个字！！！");
			return -1;
		}
		
		try {
			result = cm.createComment(text, id, comment_ori);
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		
		if (result != null) {
			return 1;
		}
		return -1;
	}

	/**
	 * 
	 * @return
	 */
	private int reply() {
		Comments cm = new Comments();
		// cid 要删除的评论ID，只能删除登录用户自己发布的评论
		Comment result = null;
		try {
			if(is_comment == 0)
				result = cm.replyComment(replyComment.getIdstr(), replyComment
					.getStatus().getId(), textArea.getText());
			else {
				result = cm.replyComment(replyComment.getIdstr(), replyComment
						.getStatus().getId(), textArea.getText(), 0, 1);
			}
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		replyComment = null;
		if (result != null) {
			return 1;
		}
		return -1;
	}

	public JTextArea getCommentTextArea() {
		downTabbedPane.setSelectedIndex(1);
		return textArea;
	}
	/**
	 * 
	 * @param replyComment
	 */
	public void setReplyComment(Comment replyComment) {
		this.replyComment = replyComment;
	}
	/**
	 * 
	 * @param type
	 * @return
	 */
	public JTextArea getTextArea(String type) {
		SetSelectedIndex(type);
		return textArea;
	}
	/**
	 * 
	 * @author Jack_Tan
	 *
	 */
	private class Clicked extends Thread {
		@Override
		public void run() {
			String reason = textArea.getText();
			String statusId = status.getId();
			int isSuccessed = -1;
			boolean isRetweeted = (status.getRetweetedStatus() != null) ? true : false;
			if (textArea.equals(repostTextArea)) {// 是转发的选项卡
				if (isRetweeted) {// 不是原创微博
					reason += "//@" + status.getUser().getName() + ":"
							+ status.getText();
				}
				isSuccessed = repost(statusId, reason, is_comment);
			} else if (textArea.equals(commentTextArea)) {// 是转发的选项卡
				isSuccessed = aboutComment(statusId, reason, isRetweeted);
			}
			showOptionPane(isSuccessed);
		}
	}
	
}
