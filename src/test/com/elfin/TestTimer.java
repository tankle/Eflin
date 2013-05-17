package test.com.elfin;


import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.junit.Test;

import weibo4j.Account;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;


public class TestTimer  {

	@Test
	public void testJavaTimer(){
		System.out.println("helloworld");
		int a,b;
		a = 2; b = 3;
		
		testSchedule();
		try {
			Thread.sleep(4 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public void testSchedule(){
		
		Integer cachetime = 1000 * 3;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println(new Date());
			}
		}, cachetime);
		
//		16.public class TimerDo {   
//		17.    public static void main(String[] args) {   
//		18.        Integer cacheTime = 1000 * 3;   
//		19.        Timer timer = new Timer();   
//		20.        // (TimerTask task, long delay, long period)任务，延迟时间，多久执行   
//		21.        timer.schedule(new TimerTask() {   
//		22.  
//		23.            @Override  
//		24.            public void run() {   
//		25.                System.out.println(new Date());   
//		26.            }   
//		27.        }, 1000, cacheTime);   
//		28.    }   
//		29.}  
	}
	

}
