package cn.zjl.application;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;

public class MyApplication extends Application {

	public int currentViewNumber = 0;
	public String currentCity = null;
	public String [] cityCollection 
	//= {"1","2","3"}
	;
	public int witchNum = 1;
	public String city1
	//= "City1"
	;
	
	public String  city2
	//= null
	;
	public String  city3
	//= null
	;
	public String  city4
	//= null
	;

	
	public HashMap<String, Object> cityData1;
	public HashMap<String, Object> cityData2;
	public HashMap<String, Object> cityData3;
	public HashMap<String, Object> cityData4;
	
	public ArrayList<HashMap<String, Object>> cityDataArray1 = null;
	public ArrayList<HashMap<String, Object>> cityDataArray2 = null;
	public ArrayList<HashMap<String, Object>> cityDataArray3 = null;
	public ArrayList<HashMap<String, Object>> cityDataArray4 = null;
	
	 public ArrayList<HashMap<String, Object>> activityCity = null;//临时集合

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
//		city1= "香港";
//		city2= "北京";
//		city3= "天津";
//		city4= "澳门";用于最初试验的几个城市
		
	
	}

}
