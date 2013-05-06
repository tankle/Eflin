package com.elfin.ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import weibo4j.Timeline;
import weibo4j.model.WeiboException;

import com.elfin.main.Main;
import com.elfin.oauth.OAuth2Code;



public class UpdateStatus extends JDialog {
	private static final long serialVersionUID = 4129242658816167905L;
	private JTextArea textArea;
	private JLabel tip;
	private static UpdateStatus instance;
	public static UpdateStatus getInstance() {
		if (instance == null) {
			instance = new UpdateStatus();
		} else{
			instance.setVisible(true);
		}
		return instance;
	}
	
	private UpdateStatus() {
		super();
		setTitle("分享你的新鲜事...");
		setModalityType(JDialog.DEFAULT_MODALITY_TYPE);// DEFAULT_MODALITY_TYPE
		setLayout(new BorderLayout());
		setSize(500, 200);
		setLocationRelativeTo(OAuth2Code.getMainDialog());
		setResizable(false);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		init();
	}

	private void init() {
		initTextArea();
		add(getScrollPane(), BorderLayout.CENTER);
		add(getDownPanel(), BorderLayout.SOUTH);
		setVisible(true);
	}

	private void initTextArea() {
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				invoke();
			}

			public void insertUpdate(DocumentEvent arg0) {
				invoke();
			}

			public void removeUpdate(DocumentEvent arg0) {
				invoke();
			}
		});
	}

	private JScrollPane getScrollPane() {
		JScrollPane jsp = new JScrollPane(textArea);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(jsp, BorderLayout.CENTER);
		return jsp;
	}

	private JPanel getDownPanel() {
		JPanel downPanel = new JPanel(new FlowLayout());
		tip = new JLabel("(你还可输入140个字。)");// +status.getUser().getName()
		tip.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		downPanel.add(tip);

		JButton sendPic = new JButton("发图");
		sendPic.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});
		downPanel.add(sendPic);
		JButton submit = new JButton("确定");
		submit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String content = textArea.getText();
				if (content.equals("")) {
					JOptionPane.showMessageDialog(null, "内容不能为空！", "错误",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				boolean isSuccess = update(content);

				if (isSuccess) {
					JOptionPane.showMessageDialog(null, "操作成功！");
				} else {
					JOptionPane.showMessageDialog(null, "操作失败，请稍后再试！", "错误",
							JOptionPane.ERROR_MESSAGE);
				}
				dispose();
			}
		});

		downPanel.add(submit);
		return downPanel;
	}

	private void invoke() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				 int num = 140 - textArea.getText().length();
				 tip.setText("(你还可输入" + num + "个字。)");
				 tip.validate();
			}
		});
	}

	private boolean update(String content) {
		Timeline tl = new Timeline();
		try {
			return tl.UpdateStatus(content) != null;
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return false;
	}
}
