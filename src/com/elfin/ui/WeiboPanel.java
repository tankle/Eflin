package com.elfin.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

/**
 * StatusPanel及CommentPanel的父类
 * 
 * @author Jack_Tan
 *
 */
public abstract class WeiboPanel extends JPanel implements Runnable{

	private static final long serialVersionUID = -6507299543942142952L;
	protected long lastId = Long.MAX_VALUE;
	protected boolean loading;
	protected TipDialog tip;
	protected GridBagConstraints gbc;
	protected int page;
	protected Thread thread;
	
	public WeiboPanel() {
		super();
//		init();
	}
	
	protected void init(){
		setBackground(Color.white);
		setLayout(new GridBagLayout());// new GridLayout(10,10)
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 10, 5, 10);
		gbc.gridy = 0;
		page = 1;
		addList();
	}
	
	@SuppressWarnings("deprecation") 
	void addList() {
		if(thread != null && thread.isAlive()){
			thread.stop();	
			thread = new Thread(this);
		}else {
			thread = new Thread(this);
		}
		thread.start();		
	}

//	protected abstract void getType();
	
	protected void refresh() {
		removeAll();
		lastId = Long.MAX_VALUE;
		gbc.gridy = 0;
		page = 1;//TODO 评论中用到
	//	getType();
		addList();
	}
	
	
	public boolean isLoading() {
		return loading;
	}

	public void setLoading(boolean loading) {
		this.loading = loading;
	}



}
