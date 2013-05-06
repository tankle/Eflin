package com.elfin.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import weibo4j.model.Comment;

import com.elfin.oauth.OAuth2Code;

/**
 * 放置一条评论的JPanel，继承自OnePanel
 * 
 * @author Jack_Tan
 *
 */
public class OneComment extends OnePanel {
	private static final long serialVersionUID = 3479474219801367267L;
	private CommentType type;
	
	
	/**
	 * CommentType的枚举commentType
	 */
	protected enum CommentType{
		MY_COMMENTS("评论"),
		All_COMMENTS("全部评论");
		private String name;
		CommentType(String name){
			this.name = name;
		}
		public String gettypeName(){
			return name;
		}
	}
	
	public OneComment(Comment comment, CommentType type) {
		super();
		this.type = type;
		setLayout(new BorderLayout());
		//创建场地容器的内层容器 
		JPanel inner = new JPanel();
		inner.setLayout(null);
		inner.setBackground(Color.white);
		inner.setBorder(border);

		//头像的
		UserHeadImg userHead = new UserHeadImg(comment.getUser());
		userHead.setBounds(locX+5, locY+5, headImgWidth, headImgHeight);
		add(userHead);
		locX += headImgWidth + 15;
		
		int width = allWidth - locX - 5;
		JTextPane textPane = getCommentTextPane(comment, locX, locY, width);
		inner.add(textPane);
		Dimension dm = textPane.getPreferredSize();
		
		locY += dm.getHeight();//下一个组件的位置
		inner.add(initLabels(comment, allWidth, locY));//加标签
		locY = locY > 60 ? locY + 5 : 60;
		if(!type.equals(CommentType.All_COMMENTS)){
			JPanel statusPanel = new OneStatus(comment.getStatus(), 
					OneStatus.StatusType.STATUS_IN_COMMENT);
			statusPanel.setLocation(locX - headImgWidth, locY);
			dm = statusPanel.getPreferredSize();
			statusPanel.setSize(dm);
			inner.add(statusPanel);
			locY += dm.getHeight();
		}
		add(inner);
		allHeight = locY > 60 ? locY+5 : 60;
		setPreferredSize(new Dimension(allWidth, allHeight));
	}
	
	private JTextPane getCommentTextPane(Comment comment, int locX, int locY,
			int width) {
		JTextPane textPane = initTextPanel();
		// 发微薄的用户名字 +微博内容
		String usernameAndText = getNameAndText(comment);
		//时间和来源
		String dateAndSource = getDateAndSource(comment);
		textPane.setText(usernameAndText + dateAndSource);
		textPane.setSize(width,
				textPane.getPreferredSize().height);
		// TODO 此时statusTextPanel.getPreferredSize().height只含有status + lastText
		int textPaneHeight = textPane.getPreferredSize().height;
		// TODO 通过setText就把imgURL也放上JTextPane上了
		textPane.setBounds(locX, locY, width,
				textPaneHeight);//TODO 纳闷这个该为allHeight也能正常显示！！！！！！！！！！！！！！！！
		textPane.setPreferredSize(new Dimension(width,
				textPaneHeight));
		return textPane;
	}
	/**
	 * 发评论的用户名字 +评论内容
	 * @param status
	 * @param isRetweeted
	 * @return
	 */
	private String getNameAndText(Comment comment){
		String samePart = "<font face=微软雅黑 size=4><a href=\"@"
			+ comment.getUser().getName() + "\">";
		String usernameAndText = samePart + comment.getUser().getName() + "</a>" + "："
					+ comment.getText();
		return usernameAndText;
	}
	/**
	 * 获取微博的时间和来源
	 * @param comment
	 * @return
	 */
	private String getDateAndSource(Comment comment){
		String dateAndSource = "<br/>"
			+ getCreateDate(comment.getCreatedAt()) + "  通过";
		String source = comment.getSource();// 来源
		dateAndSource += source + "</font></b>";
		return dateAndSource;
	}
	
	private JPanel initLabels(Comment comment, int locX, int templocY) {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBackground(Color.white);
		if(type.equals(CommentType.MY_COMMENTS)){
			panel.add(new MyLabel(MyLabel.DELETE, comment));
		}
		if(!comment.getUser().equals(OAuth2Code.getUser())){
			panel.add(new MyLabel(MyLabel.REPLY, comment));
		}
		Dimension dm = panel.getPreferredSize();
		panel.setSize(dm);
		panel.setLocation((int)(locX - dm.width - 10), templocY);
		locY += dm.height;
		return panel;
	}
}
