package com.elfin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import weibo4j.util.Log;

/**
 * 读和保存用户的登陆数据
 * 
 * @author Jason Tan
 * E-mail: tankle120@gmail.com
 * Create on：2013-5-16
 *
 */
public class FileUtil {
	
	/**
	 * 
	 * @param objects
	 */
	public static void write(String token) {
		/*
		try {
			FileOutputStream fos = new FileOutputStream(USERS);
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(fos);
			oos.writeObject(token);
			oos.flush();
			oos.close();
			Log.logInfo("数据 已写入！");
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		try {
			deleteFile(WeiboConstants.USER_TOKEN_FILE); 
			FileWriter fw = new FileWriter(WeiboConstants.USER_TOKEN_FILE, false);
			fw.write(token);
			fw.flush();
			fw.close();
			Log.logInfo("数据 已写入:" + token);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 登出
	 */
	public static void deleteToken(){
		deleteFile(WeiboConstants.USER_TOKEN_FILE);
	}
	/**
	 * delete file
	 * @param filename
	 */
	private static void deleteFile(String filename) {
		   File myDelFile = new File(filename); 
		   try {
			   if(myDelFile.exists())
				   myDelFile.delete(); 
		   } 
		   catch (Exception e) { 
		       Log.logInfo("删除文件操作出错"); 
		       e.printStackTrace(); 
		   }
	}
	/**
	 * 读取
	 * @return
	 */
	public static String read() {
		/*
		try {
			FileInputStream fis = new FileInputStream(USERS);
			ObjectInputStream oin = new ObjectInputStream(fis);
			String temp = (String) oin.readObject();
			fis.close();
			oin.close();
			return temp;
		} catch (EOFException e) {
			Log.logInfo("data 文件夹下的user.wb文件为空！");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		*/
		File file = new File(WeiboConstants.USER_TOKEN_FILE);
		String str = "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				str += tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			Log.logInfo("data 文件夹下的user.wb文件为空  或者不存在！");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return str;
	}
	
}
