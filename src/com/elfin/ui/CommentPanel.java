package com.elfin.ui;

import java.util.List;

import javax.swing.JComboBox;

import weibo4j.Comments;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;

import com.elfin.ui.OneComment.CommentType;

/**
 * 所有有关“评论”的父类
 * 
 * @author Jack_Tan
 *
 */
public abstract class CommentPanel extends WeiboPanel {
	private static final long serialVersionUID = 2641158468556981897L;
	protected static final String COMMENT = "所有的评论";
	protected static final String COMMENT_TO_ME = "收到的评论";
	protected static final String COMMENT_BY_ME = "发出的评论";
	protected Comments comments = new Comments();
	protected CommentType type;
	protected JComboBox comboBox;
	protected Thread thread;
	
	
	protected CommentPanel(){
		super();
		
	}
	
//	@Override
//	protected void getType() {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void run() {
		setLoading(true);
		// showTip("正在获取数据...");
		if(page == 0){
			page = 1;
		}
		if(page == 1 && type.equals(CommentType.MY_COMMENTS)){
			if(comboBox == null){
				initComboBox();
			}
			add(comboBox , gbc);
			++gbc.gridy;
		}
		
		List<Comment> list;
		list = getCommentWapper().getComments();

		int statusCount = list.size();
		
		for (int i = 0; i < statusCount; ++i) {
			Comment comment = list.get(i);
			long id = comment.getId();
			if (id == lastId) {
				continue;
			}
			lastId = id;
			OneComment oneComment = new OneComment(comment, type);
			add(oneComment, gbc);
			getParent().validate();
			++gbc.gridy;
			// showTip("正在载入\"" + status.getUser().getName() + "\"的微博("
			// + (i+1) + "/" + statusCount + ")");
		}
//		//当没有消息时也要更新界面
//		if(0 == statusCount)
//			getParent().validate();
		
//		tip.dispose();
		++page;
		setLoading(false);		
	}
	
	protected abstract CommentWapper getCommentWapper();
	protected void initComboBox(){	}
}
