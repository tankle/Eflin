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

import com.elfin.dao.AccessTokenDao;
import com.elfin.oauth.OAuth2Code;
import com.elfin.ui.WeiboConstants;


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

	private void init() {
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

		auth = new JButton("授权");
	//	auth.setSize(50, 20);

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

		new Thread() {
			@Override
			public void run() {
				Log.logInfo("Logining...");
				String accessToken = AccessTokenDao.read();
				if (accessToken != null) {
					try {
						new OAuth2Code(accessToken);
						return;
					} catch (WeiboException e) {
						if (e.getErrorCode() != 21325) {
							return;
						}
						JOptionPane.showMessageDialog(null,
								"提供的Access Grant是无效的、过期的或已撤销的。需要再次授权");
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


}
