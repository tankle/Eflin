package com.elfin.ui;

import weibo4j.model.CommentWapper;
import weibo4j.model.Paging;
import weibo4j.model.WeiboException;

import com.elfin.ui.OneComment.CommentType;

public class CommentById extends CommentPanel {
	private static final long serialVersionUID = -2196895232538464922L;
	private String id;
	public CommentById(String id){
		super();
		this.id = id;
		//TODO 这个在getCommentWapper之前就已经运行
		this.type = CommentType.All_COMMENTS;
		init();
	}
	
	protected CommentWapper getCommentWapper() {
		CommentWapper commentWapper = null;
		try {
			if(lastId == Long.MAX_VALUE){
				commentWapper = comments.getCommentById(id, new Paging(1), 0);
			}else{
				commentWapper = comments.getCommentById(id, lastId);
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return commentWapper;
	}
//	
//	@Override
//	protected void getType() {
//		this.type = CommentType.All_COMMENTS;
//	}
}
