package com.elfin.dao;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import weibo4j.util.Log;

public class AccessTokenDao {
	private final static String USERS = "data/users.wb";
	public static void write(String objects) {
		try {
			FileOutputStream fos = new FileOutputStream("data/users.wb");
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(fos);
			oos.writeObject(objects);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String read() {
		try {
			FileInputStream fis = new FileInputStream(USERS);
			ObjectInputStream oin = new ObjectInputStream(fis);
			String temp = (String) oin.readObject();
			fis.close();
			oin.close();
			return temp;
		} catch (EOFException e) {
			Log.logInfo("data 文件夹下的user.wb文件没有为空！");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
