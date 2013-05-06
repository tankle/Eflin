package com.elfin.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JComboBox;

import weibo4j.Comments;
import weibo4j.Timeline;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

import com.elfin.ui.OneComment.CommentType;
import com.elfin.ui.OneStatus.StatusType;

public class Mentions extends WeiboPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6161930942924131774L;

	private static final Timeline tm = new Timeline();
	private static final Comments comments = new Comments();
	private static final String MENTION_STATUS = "@我的微博";
	private static final String MEMTION_COMMENTS = "@我的评论";
	private JComboBox comboBox;
	private String selectedItem;
	
	
	public Mentions(){
		super();
		
		this.selectedItem = MENTION_STATUS;

		/**
		 * 这两条语句不能颠倒
		 */
		initComboBox();
		init();
		
		
		
		
	
	}
	
	protected enum MentionsType{
		MENTION_STATUS("@我的评论"),
		MEMTION_COMMENTS("@我的微博");
		private String name;
		MentionsType(String name){
			this.name = name;
		}
		public String getTypeName(){
			return name;
		}
	}
	

	private void initComboBox() {
		Vector<String> selectItem = new Vector<String>();
		selectItem.add(MENTION_STATUS);
		selectItem.add(MEMTION_COMMENTS);
		comboBox = new JComboBox(selectItem);
		
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					removeAll();
					selectedItem = (String) comboBox.getSelectedItem();
					page = 1;
					gbc.gridy = 0;
					setLoading(true);
					addList();
				}
			}
		});
	}

	@Override
	public void run() {
		setLoading(true);
		if(page == 1){
			add(comboBox, gbc);
			validate();
			++gbc.gridy;
		}
		
		if(selectedItem.equals(MENTION_STATUS)){//@我的微博
			StatusWapper statusWapper = null;
			try {
				statusWapper = tm.getMentions(new Paging(page), 0, 0, 0);
			} catch (WeiboException e) {
				e.printStackTrace();
			}
			for (Status s : statusWapper.getStatuses()) {
				if(s.getUser() == null){
					System.out.println(s.getId() + "Weibo获取不到用户！！！");
					
					continue;
				}
//				System.out.println("@我的微博 --> " + s.toString());
				
				OneStatus oneStatus = new OneStatus(s, StatusType.FRIEND_TIMELINE);
				add(oneStatus, gbc);
				getParent().validate();
				++gbc.gridy;
			}
		}
		else{//@我的评论
			CommentWapper commentWapper = null;
			try {
				commentWapper = comments.getCommentMentions(new Paging(page), 0, 0); 
			} catch (WeiboException e) {
				e.printStackTrace();
			}
			for (Comment c : commentWapper.getComments()) {
				if(c.getUser() == null){
					System.out.println("Weibo获取不到用户名！！！");
					continue;
				}
				OneComment oneComment = new OneComment(c, CommentType.MY_COMMENTS);
				add(oneComment, gbc);
				getParent().validate();
				++gbc.gridy;
			}
		}
		++page;
		setLoading(false);
	}
//
//	@Override
//	protected void getType() {
//		// TODO Auto-generated method stub
//		
//	}

}
