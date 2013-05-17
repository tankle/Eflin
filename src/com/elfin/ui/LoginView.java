package com.elfin.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import weibo4j.model.WeiboException;
import weibo4j.util.Log;
import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;

import com.elfin.oauth.OAuth2Code;
import com.elfin.util.FileUtil;
import com.elfin.util.WeiboConstants;

/**
 * 登陆界面
 * 
 * @author Jason Tan
 * E-mail: tankle120@gmail.com
 * Create on：2013-5-16
 *
 */
public class LoginView extends JFrame {

	private static final int WIDTH = 300;
	private static final int HEIGHT = 150;
	private JButton auth = null;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4413630923718806265L;

	public LoginView() {

		init();
	}

	public void init() {
		this.setTitle("登录");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	
		Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();

		this.setLocation(dm.width / 2 - WIDTH / 2, dm.height / 2 - HEIGHT / 2);
		this.setSize(WIDTH, HEIGHT);
		// setLayout(null);
		setBackground(Color.WHITE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		;

		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());

		this.setContentPane(content);

		auth = new JButton();
		
		String accessToken = FileUtil.read();
		Log.logInfo("The access token is " +accessToken);
		if(null != accessToken && "" != accessToken){
			auth.setText("登陆");
		}else{
			auth.setText("授权");
		}

		content.add(auth,BorderLayout.CENTER);

		this.setVisible(true);

		auth.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				getToken();
			}
		});

	}

	protected void getToken() {
		this.dispose();
		
//		this.setVisible(false);
//		
		new Thread() {
			@Override
			public void run() {
				
				String accessToken = FileUtil.read();
				Log.logInfo("Logining... The access token is " + accessToken );
				if (accessToken != null && "" != accessToken) {
					try {
						new OAuth2Code(accessToken);
						return;
					} catch (WeiboException e) {
						if (e.getErrorCode() != 21325) {
							return;
						}
						JOptionPane.showMessageDialog(null,
								"提供的Access Grant是无效的、过期的或已撤销的。需要再次授权");
						//删除token
						FileUtil.deleteToken();
					}

				}

				NativeInterface.open();
				UIUtils.setPreferredLookAndFeel();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						new OAuth2Code();
					}
				});
				NativeInterface.runEventPump();

			}

		}.start();
	}
//
//	public void start() {
//		this.setVisible(true);
//	}


}
