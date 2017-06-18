package cn.zjl.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import cn.zjl.application.MyApplication;
import cn.zjl.example.androidweather.R;
import cn.zjl.example.androidweather.WeatherMainActivity;
import cn.zjl.sharedpreferences.SharedPreferencesUtils;
import cn.zjl.tools.NumToBeImage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Fragment4 extends Fragment implements OnClickListener{

	protected Context mContext;
	MyApplication myApplication;
	String result;
//	ToolTwo toolTwo;

	RelativeLayout relativeLayout1;

	TextView tv_delete1,//删除
	tv_fragment_air_quality1_2,tv_fragment_air_pm1_2,tv_fragment_curPm1_2,//前三个指数
	tv_fragment_wind_speed1_2,//风力指数
	tv_fragment_water1_2,//湿度
	weather_fragment_text,//阴晴字体
	tv_fragment_max1,tv_fragment_min1,//最高最低温度字体
	tv_fragment_temperature,//温度字体
	tv_change1,//城市切换

	textViewAdd1,//添加城市

	tv_out_time1//发布时间
	;

	ImageView  img_fragment_weather1_1//天气大图标

	;

	//HashMap<String, Object> myApplication.cityData4;

	public WeatherMainActivity weatherMainActivityFragment1;



	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_city4, container, false);

		mContext = getActivity();//用于application实例化
		myApplication = (MyApplication) ((Activity) mContext).getApplication();
		weatherMainActivityFragment1 = (WeatherMainActivity) getActivity();
//		toolTwo = new ToolTwo();

		relativeLayout1 = (RelativeLayout) view.findViewById(R.id.relativeLayout1);//不需要绑定监听器

		tv_delete1 = (TextView) view.findViewById(R.id.tv_delete1);
		tv_delete1.setOnClickListener(this);

		textViewAdd1 = (TextView) view.findViewById(R.id.textViewAdd1);
		textViewAdd1.setOnClickListener(this);

		tv_change1	 = (TextView) view.findViewById(R.id.tv_change1);
		tv_change1.setOnClickListener(this);


		tv_fragment_air_quality1_2 = (TextView) view.findViewById(R.id.tv_fragment_air_quality1_2);		
		tv_fragment_air_pm1_2 = (TextView) view.findViewById(R.id.tv_fragment_air_pm1_2);
		tv_fragment_curPm1_2 = (TextView) view.findViewById(R.id.tv_fragment_curPm1_2);

		tv_fragment_wind_speed1_2 = (TextView) view.findViewById(R.id.tv_fragment_wind_speed1_2);

		tv_fragment_water1_2 = (TextView) view.findViewById(R.id.tv_fragment_water1_2);

		weather_fragment_text = (TextView) view.findViewById(R.id.weather_fragment_text);
		tv_fragment_max1 = (TextView) view.findViewById(R.id.tv_fragment_max1);
		tv_fragment_min1 = (TextView) view.findViewById(R.id.tv_fragment_min1);
		tv_fragment_temperature = (TextView) view.findViewById(R.id.tv_fragment_temperature);
		tv_out_time1 = (TextView) view.findViewById(R.id.tv_out_time1);


		img_fragment_weather1_1 = (ImageView) view.findViewById(R.id.img_fragment_weather1_1);


		if(myApplication.city4.equals("错误")||myApplication.city4==null)
		{
			relativeLayout1.setVisibility(relativeLayout1.GONE);//撤销
			textViewAdd1.setVisibility(tv_delete1.VISIBLE);//可见
		//	weatherMainActivityFragment1.allInfoToBeFullOrNull();
		}else{
	//		myApplication.cityData4= toolTwo.info(myApplication.city4);
	//		myApplication.cityDataArray4 = (ArrayList<HashMap<String, Object>>) myApplication.cityData4.get("weather");
			
			relativeLayout1.setVisibility(relativeLayout1.VISIBLE);
			textViewAdd1.setVisibility(tv_delete1.GONE);
	     	getWeatherInfoFoforFragment();//加载fragment控件数据
		//	weatherMainActivityFragment1.getWeather(3);
		}
		return view;
	}
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(myApplication.city4.equals("错误")||myApplication.city4==null)
		{
			relativeLayout1.setVisibility(relativeLayout1.GONE);//撤销
			textViewAdd1.setVisibility(tv_delete1.VISIBLE);//可见
		//	weatherMainActivityFragment1.allInfoToBeFullOrNull();
		}else{
//			myApplication.cityData4= toolTwo.info(myApplication.city4);
//			myApplication.cityDataArray4 = (ArrayList<HashMap<String, Object>>) myApplication.cityData4.get("weather");
			
			relativeLayout1.setVisibility(relativeLayout1.VISIBLE);
			textViewAdd1.setVisibility(tv_delete1.GONE);
	     	getWeatherInfoFoforFragment();
		//	weatherMainActivityFragment1.getWeather(3);
		}
	}
	private void getWeatherInfoFoforFragment() {
		// TODO Auto-generated method stub
		tv_fragment_air_quality1_2.setText(myApplication.cityData4.get("quality").toString());	
		tv_fragment_air_pm1_2.setText(myApplication.cityData4.get("pm252").toString()+"μg/m³");	
		tv_fragment_curPm1_2.setText(myApplication.cityData4.get("curPm").toString()+"μg/m³");	

		tv_out_time1.setText(myApplication.cityData4.get("time").toString());	

		tv_fragment_wind_speed1_2.setText(myApplication.cityData4.get("windPower").toString()+"，"+ myApplication.cityData4.get("winDirect").toString()+"，风速"+myApplication.cityData4.get("windSpeed").toString());//风力指数3个信息拼接

		weather_fragment_text.setText(myApplication.cityData4.get("curWeatherText").toString());

		tv_fragment_temperature.setText(myApplication.cityData4.get("temperature").toString());


		ArrayList<HashMap<String, Object>> fragmentArrayList = (ArrayList<HashMap<String, Object>>) myApplication.cityData4.get("weather");

		tv_fragment_water1_2.setText(fragmentArrayList.get(0).get("week").toString());//湿度改为星期

		tv_fragment_max1.setText(fragmentArrayList.get(0).get("dayDushu").toString());
		tv_fragment_min1.setText(fragmentArrayList.get(0).get("nightDushu").toString());

		img_fragment_weather1_1.setImageResource(new NumToBeImage().NumToBeImageWay((fragmentArrayList.get(0).get("dayImageNum").toString())));
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_delete1:

			myApplication.city4 = "错误";
			SharedPreferencesUtils.inputValue(mContext, "city4", "错误");
			relativeLayout1.setVisibility(relativeLayout1.GONE);//撤销
			textViewAdd1.setVisibility(tv_delete1.VISIBLE);//可见
			weatherMainActivityFragment1.allInfoToBeFullOrNull();
			weatherMainActivityFragment1.tv_top.setText("");
			break;


		case R.id.tv_change1:

			weatherMainActivityFragment1.startTuChangeCity(4);

			break;

		case R.id.textViewAdd1:

			relativeLayout1.setVisibility(relativeLayout1.VISIBLE);
			textViewAdd1.setVisibility(tv_delete1.GONE);
			weatherMainActivityFragment1.startTuChangeCity(4);

			break;


		default:
			break;
		}
	}
}
