package com.elfin.ui;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollPane;
import javax.swing.JViewport;

/**
 * 重载JScrollPane的一个类，方便用于微博客户端的实现
 * 设置水平滚动条从不出现
 * @author Jack_Tan
 *
 */
public class MyScrollPane extends JScrollPane{
	private static final long serialVersionUID = -4740514460083277432L;
	private WeiboPanel weiboPanel;
	
	public MyScrollPane(final WeiboPanel weiboPanel){
		super(weiboPanel);
		this.weiboPanel = weiboPanel;
		
		setBounds(0, 0, MainDialog.WIDTH - 20, MainDialog.HEIGHT - 60);
		//设置水平滚动条从不出现
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		verticalScrollBar.setUnitIncrement(25);
		verticalScrollBar.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				// getMaximum()滚动条的最大值为最大跨度。
				// getVisibleAmount()可见的大小
				int currentValue = getVerticalScrollBar().getMaximum()
						- getVerticalScrollBar().getVisibleAmount();
				if (e.getValue() != 0 && e.getValue() == currentValue 
					 && !weiboPanel.isLoading()) {
					weiboPanel.addList();
				}
			}
		});
	}

	public MyScrollPane() {
		super();
		//setBounds(0, 0, 480, 450);
		setBounds(0, 0, MainDialog.WIDTH - 20, MainDialog.HEIGHT - 60);
		// 设置水平滚动条从不出现
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		verticalScrollBar.setUnitIncrement(25);
		
		verticalScrollBar.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				// getMaximum()滚动条的最大值为最大跨度。
				// getVisibleAmount()可见的大小
				int currentValue = getVerticalScrollBar().getMaximum()
						- getVerticalScrollBar().getVisibleAmount();
				// 注意这里的e.getValue() != 0
				if (e.getValue() != 0 && e.getValue() == currentValue
						&& weiboPanel != null && !weiboPanel.isLoading()) {
					weiboPanel.addList();
				}
			}
		});
	}

	public void setViewportView(WeiboPanel weiboPanel) {
		super.setViewportView(weiboPanel);
		this.weiboPanel = weiboPanel;
	}
}