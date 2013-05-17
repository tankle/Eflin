package com.elfin.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JComboBox;

import com.elfin.ui.OneComment.CommentType;

import weibo4j.model.CommentWapper;
import weibo4j.model.Paging;
import weibo4j.model.WeiboException;

/**
 * 我的评论
 * 
 * @author Jason Tan
 * E-mail: tankle120@gmail.com
 * Create on：2013-5-17
 *
 */
public class MyComment extends CommentPanel {

	private static final long serialVersionUID = -2287946852738315905L;
	private String selectedItem;
	
	
	public MyComment() {
		super();
		type = CommentType.MY_COMMENTS;
		init();
	}
	
	@Override
	protected CommentWapper getCommentWapper() {

		if(selectedItem == null)
			this.selectedItem = COMMENT;
		CommentWapper commentWapper = null;
		try {
			System.out.println(page + " --- " + selectedItem);
			if (selectedItem.equals(COMMENT)) {
				commentWapper = comments.getCommentTimeline(new Paging(page));
			} else if (selectedItem.equals(COMMENT_TO_ME)) {
				commentWapper = comments.getCommentToMe(new Paging(page), 0, 0);
			} else if (selectedItem.equals(COMMENT_BY_ME)) {
				commentWapper = comments.getCommentByMe(new Paging(page), 0);
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return commentWapper;
	}

//	@Override
//	protected void getType() {
//		type = CommentType.MY_COMMENTS;
//	}
//	
	protected void initComboBox(){
		Vector<String> selectItem = new Vector<String>();
		selectItem.add(COMMENT);
		selectItem.add(COMMENT_TO_ME);
		selectItem.add(COMMENT_BY_ME);
		
		comboBox = new JComboBox(selectItem);
		//选择按钮监听事件
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					removeAll();
					//getParent().repaint();
					//getParent().validate();
					selectedItem = (String) comboBox.getSelectedItem();
					page = 1;
					gbc.gridy = 0;
//					setLoading(true);
					addList();
				}
			}
		});
	}
	
}
