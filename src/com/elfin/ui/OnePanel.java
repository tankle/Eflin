package com.elfin.ui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.Border;

/**
 * 放置一条微博,或者一条评论的父类
 * 
 * @author Jack_Tan
 *
 */
public class OnePanel extends JPanel {
	private static final long serialVersionUID = -4266353309439413832L;

	protected static Border border = BorderFactory.createEtchedBorder();
	protected int allWidth = MainDialog.WIDTH - 50;
	protected int allHeight = 0;
	protected int headImgWidth = 50, headImgHeight = 50;
	protected int locX = 2, locY = 2;
	public OnePanel(){
		
	}
	
	/**
	 *初始化每个JTextPane
	 * @return JTextPane
	 */
	protected JTextPane initTextPanel() {
		JTextPane textPanel = new JTextPane();
		textPanel.addHyperlinkListener(new MyHyperlinkListener());
		textPanel.setEditable(false);
		textPanel.setContentType("text/html");
//		textPanel.setBackground(new Color(0xeeeeee));
		return textPanel;
	}
	
	/**
	 * 把微博/评论等等发送的时间(Date类型)"格式化"，并转化成"String类型"。 为静态类型，这个方法在CommentPanel等也使用到。
	 * 
	 * @param 发送的时间
	 *            (date类型)
	 *@return 发送的时间(String类型)
	 */
	protected static String getCreateDate(Date createDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(createDate);
	}
	/**
	 * 获取图片的宽度 通用方法
	 * 
	 * @param 图片的地址
	 *            (String类型)
	 *@return 图片的高度Height(int类型)
	 */
	protected int getImgHeight(String imgURL) throws NullPointerException {
		java.net.URL url = null;
		try {
			url = new URL(imgURL);
		} catch (MalformedURLException e) {
			System.out.print("载入图片失败 MalformedURLException");
		}
		// TODO BufferedImage 子类描述具有可访问图像数据缓冲区的 Image
		// 使用BufferedImage 才能再未显现图片时知道图片的大小
		int height = 0;
		BufferedImage bi = null;
		try {
			bi = javax.imageio.ImageIO.read(url);
			height = bi.getHeight();
		} catch (IOException e) {
			System.out.print("载入图片失败 IOException");
		} catch (java.lang.IllegalArgumentException e) {
			System.out.print("载入图片失败 IllegalArgumentException" );
		}
		return height;
	}
	
}
