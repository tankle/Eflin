package test.com.elfin;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.BeforeClass;
import org.junit.Test;

import weibo4j.Account;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.model.Status;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;
import weibo4j.util.Log;

public class testWeibo {
	private static Weibo weibo;
	static Account account ;
	private static String TOKEN = "2.00BoSNuBjR5fsD119d3c7c0bgQpA5B";
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		weibo = new Weibo();
		weibo.setToken(TOKEN);
		account = new Account();
		String uid = null;
		try {
			try {
				uid = account.getUid().get("uid").toString();
			} catch (WeiboException e) {
				e.printStackTrace();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Users um = new Users();
		User user = null;
		try {
			 user = um.showUserById(uid);
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Test
	public void testSend(){
		String statuses = "";
		System.out.println("please input the word you want to post:");
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		try {
			statuses = br.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		Timeline tm = new Timeline();
		
		try {
			Status status = tm.UpdateStatus(statuses);
			Log.logInfo(status.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}	
	}
}
