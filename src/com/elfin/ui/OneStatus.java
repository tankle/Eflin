package com.elfin.ui;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import weibo4j.model.Favorites;
import weibo4j.model.Source;
import weibo4j.model.Status;
import weibo4j.model.User;
import weibo4j.util.Log;

import com.elfin.oauth.OAuth2Code;


/**
 * 放置一条微博的JPanel
 * 
 * @author Jack_Tan
 *
 */
public class OneStatus extends OnePanel{
	private static final long serialVersionUID = -1593069338229047705L;
	private int imgHeight;
	private StatusType type;
	
	/**
	 * StatusPanel的枚举StatusType
	 */
	protected enum StatusType{
		FRIEND_TIMELINE("首页"),
		REPOST_TIMELINE("全部转发"),
		STATUS_TEXT("微博正文"),
		STATUS_IN_COMMENT("评论的微博"),
		FAVORITE("收藏的微博"), 
		USER_TIMELINE("我的微博");
		
		private String name;
		StatusType(String name){
			this.name = name;
		}
		public String gettypeName(){
			return name;
		}
	}
	
	/**
	 * 
	 * @param status
	 * @param type
	 */
	public OneStatus(Status status, StatusType type) {
		super();
		this.type = type;
		if(type.equals(StatusType.STATUS_IN_COMMENT)){//评论里含有的微博
			this.allWidth = MainDialog.WIDTH - 75;
		}
		init(status);
	}
	
	/**
	 * 
	 * @param favorite
	 * @param type
	 */
	public OneStatus(Favorites favorite, StatusType type) {
		super();
		this.type = type;
		init(favorite.getStatus());
	}

	private void init(Status status){
		setLayout(null);
		setBackground(Color.white);
		JPanel inner = getInner(status);
		setSize(allWidth, allHeight);
		setPreferredSize(new Dimension(allWidth, allHeight + 12));
		add(inner);
	}
	
	private JPanel getInner(Status status){
		JPanel innerPanel = new JPanel();
		innerPanel.setBackground(Color.white);
		innerPanel.setLayout(null);
		innerPanel.setBorder(border);
		int headImgWidth = 0;
		if(!type.equals(StatusType.STATUS_IN_COMMENT)){//不是评论里含有的微博
			// 头像
			User user = status.getUser();
			if(null == user){
				System.out.println("User is null!");
				Log.logInfo("User is null in OneStatus.getInner");
				//innerPanel = getErrorTextPane(textPane, x, y, width)
				return innerPanel;
			}
			UserHeadImg userHead = new UserHeadImg(user);
			userHead.setLocation(2, 2);
			innerPanel.add(userHead);
			Dimension headImgDms = userHead.getPreferredSize();
			headImgWidth = (int)(headImgDms.getWidth());
		}
		locX  +=  headImgWidth;
		int statusWidth = (int) (allWidth - headImgWidth - 10);
		innerPanel.add(getTextPane(status, locX, locY, statusWidth, false));
		
		if(!type.equals(StatusType.STATUS_IN_COMMENT)){//不是评论里含有的微博
			allHeight = allHeight > 55 ? allHeight : 55;
		}else{
			locX += 10;
			statusWidth -= 10;
		}
		//转发的微博
		if(!type.equals(StatusType.REPOST_TIMELINE) && status.getRetweetedStatus() != null){
			statusWidth += headImgWidth;
			Status retweetedStatus = status.getRetweetedStatus();
			innerPanel.add(getTextPane(retweetedStatus, locX - headImgWidth, 
					locY + allHeight, statusWidth, true));
			if(!type.equals(StatusType.STATUS_TEXT)) {//不是微博正文
				innerPanel.add(initLabels(retweetedStatus, locX - headImgWidth, allHeight + 3, true));
			}
		}
		if(!type.equals(StatusType.STATUS_TEXT)){//不是微博正文
			innerPanel.add(initLabels(status, allWidth, allHeight + 3, false));
		}
		innerPanel.setBounds(0, 0, allWidth, allHeight + 5);
		return innerPanel;
	}
	
	private JTextPane getTextPane(Status status, int x, int y, int width, boolean isRetweeted){
		JTextPane textPane = initTextPanel();
		if(status.getUser() == null){
			return getErrorTextPane(textPane, x, y, width);
		}
		// 发微薄的用户名字 +微博内容
		String usernameAndText = getNameAndText(status, isRetweeted);
		//图片
		String imgURL = getImg(status);
		//发微博的时间和来源
		String dateAndSource = getDateAndSource(status);
	
		textPane.setText(usernameAndText + dateAndSource);
		textPane.setSize(width,
				textPane.getPreferredSize().height + imgHeight);
		// TODO 此时statusTextPanel.getPreferredSize().height只含有status + lastText
		int textPaneHeight = textPane.getPreferredSize().height
				+ imgHeight;
		allHeight += textPaneHeight;
		textPane.setText(usernameAndText + imgURL
				+ dateAndSource);
		textPane.setBounds(x, y, width,
				textPaneHeight);//TODO 纳闷这个该为allHeight也能正常显示！！！！！！！！！！！！！！！！
		return textPane;
	}
	/**
	 * 
	 * @param status
	 * @return
	 */
	private String getImg(Status status){
		String imgUrl = "";
		if (!"".equals(status.getThumbnailPic())) {
			imgHeight = getImgHeight(status.getThumbnailPic()) + 23;
			imgUrl =  "<br /><br /><a href=\"" + status.getBmiddlePic() 
				+ "\"><img border=\"0\" src=" + status.getThumbnailPic()
				+ " /></a>";
		}
		return imgUrl;
		
	}
	/**
	 * 处理status.getUser() == null的微博
	 * @return
	 */
	private JTextPane getErrorTextPane(JTextPane textPane, int x, int y, int width){
		String sorry = "抱歉，此微博已被原作者删除。如需帮助，请联系客服。http://help.weibo.com/self/query?typeid=1034";
		textPane.setText(sorry);
		textPane.setSize(width,
				textPane.getPreferredSize().height);
		int textPaneHeight = textPane.getPreferredSize().height;
		textPane.setText(sorry);
		textPane.setBounds(x, y, width,
				textPaneHeight);//TODO 纳闷这个该为allHeight也能正常显示！！！！！！！！！！！！！
		allHeight += textPaneHeight;
		return textPane;
	}
	/**
	 * 发微薄的用户名字 +微博内容
	 * @param status
	 * @param isRetweeted
	 * @return
	 */
	private String getNameAndText(Status status, boolean isRetweeted){
		String samePart = "<font face=微软雅黑 size=4><a href=\"@"
			+ status.getUser().getName() + "\">";
		String usernameAndText = "";
		if(isRetweeted){//是转发的
			usernameAndText = samePart+ "@" + status.getUser().getName() + "</a>" + "："
			+ status.getText();
		}else{
			if(type.equals(StatusType.USER_TIMELINE)){
				usernameAndText = "<font face=微软雅黑 size=4>" + status.getText();
			}else{
			usernameAndText = samePart + status.getUser().getName() + "</a>" + "："
					+ status.getText();
			}
		}
		return usernameAndText;
	}
	/**
	 * 获取微博的时间和来源
	 * @param status
	 * @return
	 */
	private String getDateAndSource(Status status){
		String dateAndSource = "<br/>"
			+ getCreateDate(status.getCreatedAt()) + "  通过";
		Source source = status.getSource();// 来源
		dateAndSource += "<a href=\"" + source.getUrl() + "\">"
			+ source.getName() + "</font></b>";
		return dateAndSource;
	}

	/**
	 * 初始化每条微博右下角的按钮
	 * 
	 * @param status
	 * @param x
	 * @param y
	 * @param isRetweeted
	 * @return
	 */
	private JPanel initLabels(Status status, int x, int y, boolean isRetweeted) {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBackground(Color.white);
		panel.setComponentOrientation(
				ComponentOrientation.RIGHT_TO_LEFT);
		String repost_Text = MyLabel.REPOST;
		String comment_Text = MyLabel.COMMENT;
		if(!isRetweeted) {
			String text = getLabelText(repost_Text,  
					status.getRepostsCount());//转发
			panel.add(new MyLabel(text, status));
			text = getLabelText(comment_Text, 
					status.getCommentsCount());//评论
			panel.add(new MyLabel(text, status));
			
			
			if(status.isFavorited()) {//收藏
				panel.add(new MyLabel(MyLabel.UNFAVORITE,status));
			}else {
				panel.add(new MyLabel(MyLabel.FAVORITE,status));
			}
			
			if(status.getUser().equals(OAuth2Code.getUser())){//如果是自己的微博，加上删除的标签
				panel.add(new MyLabel(MyLabel.DELETE, status));
			}
			if(!status.isFavorited()){//为了使得”收藏“变”取消收藏“时，能正常显示
				panel.add(new JLabel("        "));
			}
			
		}else {
			repost_Text = MyLabel.ORIGINAL_REPOST;
			comment_Text = MyLabel.ORIGINAL_COMMENT;
			String text = getLabelText(repost_Text,  
					status.getRepostsCount());//转发
			panel.add(new MyLabel(text, status));
			text = getLabelText(comment_Text, 
					status.getCommentsCount());//评论
			panel.add(new MyLabel(text, status));
		}
		
		Dimension dm = panel.getPreferredSize();
		panel.setSize(dm);
		if(!isRetweeted){//不是转发的
			panel.setLocation((int)(x - dm.width - 10), y);
			allHeight += dm.height;
		}else{//转发的
			panel.setLocation(x, y);
			panel.setBorder(border);
		}
		return panel;
	}
	private String getLabelText(String content, int count){
		StringBuffer text = new StringBuffer(content);
		if(count != 0){
			text.append("(" + count + ")");
		}
		return text.toString();
	}

}
