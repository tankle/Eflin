package com.elfin.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * 提示语的dialog
 * 
 * @author Jack_Tan
 *
 */
public class TipDialog extends JDialog{

	private static final long serialVersionUID = -436115220368485959L;
	
	public TipDialog(String text){
		setUndecorated(true);
		JLabel label = new JLabel(text);
		
		label.setFont(new Font("微软雅黑",Font.PLAIN,18));
		label.setForeground(Color.RED);
		
		label.setLocation(3, 0);
		label.setSize(label.getPreferredSize());
		add(label);
		
		setSize(label.getPreferredSize());
		setVisible(true);
		
		addWindowListener(new WindowAdapter(){
			public void windowDeactivated(WindowEvent e) {
				dispose();
			}
		});
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.red);
		g.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 5, 5);
	}
}