package cn.zjl.example.androidweather;

import java.util.ArrayList;
import java.util.HashMap;

import cn.zjl.application.MyApplication;
import cn.zjl.example.androidweather.R;
import cn.zjl.sharedpreferences.SharedPreferencesUtils;
import cn.zjl.tools.ToolTwo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;





public class WelcomeView extends FragmentActivity implements  AnimationListener{
	MyApplication myApplication;
	ToolTwo toolTwo1;
	ToolTwo toolTwo2;
	ToolTwo toolTwo3;
	ToolTwo toolTwo4;

	ImageView iv_lunching_picture;


	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_view);

		myApplication = (MyApplication) getApplication();
		toolTwo1 = new ToolTwo();
		toolTwo2 = new ToolTwo();
		toolTwo3 = new ToolTwo();
		toolTwo4 = new ToolTwo();

		iv_lunching_picture = (ImageView) findViewById(R.id.iv_lunching_picture);
		iv_lunching_picture.setImageResource(R.drawable.weilcome_view);


		myApplication.city1 =	(String) SharedPreferencesUtils.outputValueTwo(this, "FourCitiesName", "city1", "判断值");
		myApplication.city2 =	(String) SharedPreferencesUtils.outputValueTwo(this, "FourCitiesName", "city2", "判断值");
		myApplication.city3 =	(String) SharedPreferencesUtils.outputValueTwo(this, "FourCitiesName", "city3", "判断值");
		myApplication.city4 =	(String) SharedPreferencesUtils.outputValueTwo(this, "FourCitiesName", "city4", "判断值");


		if(myApplication.city1.equals("错误")|| myApplication.city1 == null){

		}else{
			myApplication.cityData1 = toolTwo1.info(myApplication.city1);
			myApplication.cityDataArray1 = (ArrayList<HashMap<String, Object>>) myApplication.cityData1.get("weather");
		}
		if(myApplication.city2.equals("错误")|| myApplication.city2 == null){

		}else{
			myApplication.cityData2 = 	toolTwo2.info(myApplication.city2);
			myApplication.cityDataArray2 = (ArrayList<HashMap<String, Object>>) myApplication.cityData2.get("weather");
		}
		if(myApplication.city3.equals("错误")|| myApplication.city3 == null){

		}else{
			myApplication.cityData3 = 	toolTwo3.info(myApplication.city3);
			myApplication.cityDataArray3 = (ArrayList<HashMap<String, Object>>) myApplication.cityData3.get("weather");
		}
		if(myApplication.city4.equals("错误")|| myApplication.city4 == null){

		}else{
			myApplication.cityData4 = 	toolTwo4.info(myApplication.city4);
			myApplication.cityDataArray4 = (ArrayList<HashMap<String, Object>>) myApplication.cityData4.get("weather");

		}
		// 透明度渐变动画
		AlphaAnimation animation = new AlphaAnimation(0, 1);
		// 动画持续时间
		animation.setDuration(3000);
		iv_lunching_picture.startAnimation(animation);

		animation.setAnimationListener(this);

	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub
		
		
		startActivity(new Intent(WelcomeView.this,WeatherMainActivity.class));
		WelcomeView.this.finish();
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub




	}		



}
