package cn.zjl.example.androidweather;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.zjl.application.MyApplication;
import cn.zjl.city.CityListActivity;
import cn.zjl.example.androidweather.R;
import cn.zjl.fragment.Fragment1;
import cn.zjl.fragment.Fragment2;
import cn.zjl.fragment.Fragment3;
import cn.zjl.fragment.Fragment4;
import cn.zjl.fragment.FragmentAdapter;
import cn.zjl.tools.NumToBeImage;
import cn.zjl.tools.ToolTwo;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;



public class WeatherMainActivity extends FragmentActivity implements RefreshableView.RefreshListener,OnClickListener{
	public MyApplication myApplication;

	LinearLayout linearlayout_backgroud;//用于更新天气情况背景图

	ToolTwo toolTwo1;
	ToolTwo toolTwo2;
	ToolTwo toolTwo3;
	ToolTwo toolTwo4;
	
	
	
	public	SlidingMenu slidingMenu;//侧滑菜单
	public RefreshableView mRefreshableView;//下拉更新view
	public Context mContext;
	double lat;//经纬度
	double lng;
	public	ImageView img_main_pager_side,
	img_yubao1,img_yubao2,img_yubao3,img_yubao4,img_yubao5,img_yubao6,img_yubao7//周期到周六的最高最低温度对应的图片
	;
	public 	TextView tv_top,//顶部定位标签
	//侧滑菜单
	tv_sliding_current_city,tv_sliding_set,tv_sliding_more,tv_sliding_exit,tv_sliding_now_city,
	//GPS控件
	tv_local_information,//您所在城市经纬度
	//周日到周六风力情况的微风与其周一与周日的接口顺序
	tv_sun1,tv_mon1,tv_tus1,tv_wed1,tv_thus1,tv_fri1,tv_sat1,
	tv_sun,tv_mon,tv_tus,tv_wed,tv_thus,tv_fri,tv_sat,
	//周期到周六的最高最低温度
	_max_tm1,_min_tm1,	_max_tm2,_min_tm2,	_max_tm3,_min_tm3,	_max_tm4,_min_tm4, 	_max_tm5,_min_tm5, _max_tm6,_min_tm6,_max_tm7,_min_tm7,
	//预报栏之下的周一到周日textview
	tv_yubao1,tv_yubao2,tv_yubao3,tv_yubao4,tv_yubao5,tv_yubao6,tv_yubao7

	;
	public	ArrayList<Fragment>  fourFragment;//4个天气界面
	public ViewPager main_viewpager;
	public FragmentAdapter fragmentAdapter;

	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			super.handleMessage(message);
			mRefreshableView.finishRefresh();
			Toast.makeText(mContext,"刷新完毕", Toast.LENGTH_SHORT).show();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		myApplication = (MyApplication) getApplication();
		mContext = this;
		mRefreshableView = (RefreshableView) findViewById(R.id.refresh_root);
		mRefreshableView.setRefreshListener(this);
		
		toolTwo1 = new ToolTwo();
		toolTwo2 = new ToolTwo();
		toolTwo3 = new ToolTwo();
		toolTwo4 = new ToolTwo();

		linearlayout_backgroud = (LinearLayout) findViewById(R.id.linearlayout_backgroud);
		
		img_main_pager_side = (ImageView) findViewById(R.id.img_main_pager_side);//侧滑菜单弹出按键
		img_main_pager_side.setOnClickListener(this);
		img_yubao1 = (ImageView) findViewById(R.id.img_yubao1);
		img_yubao2 = (ImageView) findViewById(R.id.img_yubao2);
		img_yubao3 = (ImageView) findViewById(R.id.img_yubao3);
		img_yubao4 = (ImageView) findViewById(R.id.img_yubao4);
		img_yubao5 = (ImageView) findViewById(R.id.img_yubao5);
		img_yubao6 = (ImageView) findViewById(R.id.img_yubao6);
		img_yubao7 = (ImageView) findViewById(R.id.img_yubao7);

		tv_top = (TextView) findViewById(R.id.tv_top);//顶端
		tv_local_information = (TextView) findViewById(R.id.tv_local_information);//中部

		tv_sun1 = (TextView) findViewById(R.id.tv_sun1);
		tv_mon1 = (TextView) findViewById(R.id.tv_mon1);
		tv_tus1 = (TextView) findViewById(R.id.tv_tus1);
		tv_wed1 = (TextView) findViewById(R.id.tv_wed1);
		tv_thus1 = (TextView) findViewById(R.id.tv_thus1);
		tv_fri1 = (TextView) findViewById(R.id.tv_fri1);
		tv_sat1 = (TextView) findViewById(R.id.tv_sat1);

		tv_sun = (TextView) findViewById(R.id.tv_sun);
		tv_mon = (TextView) findViewById(R.id.tv_mon);
		tv_tus = (TextView) findViewById(R.id.tv_tus);
		tv_wed = (TextView) findViewById(R.id.tv_wed);
		tv_thus = (TextView) findViewById(R.id.tv_thus);
		tv_fri = (TextView) findViewById(R.id.tv_fri);
		tv_sat = (TextView) findViewById(R.id.tv_sat);

		//预报栏之下的控件
		tv_yubao1	= (TextView) findViewById(R.id.tv_yubao1);
		tv_yubao2	= (TextView) findViewById(R.id.tv_yubao2);
		tv_yubao3	= (TextView) findViewById(R.id.tv_yubao3);
		tv_yubao4	= (TextView) findViewById(R.id.tv_yubao4);
		tv_yubao5	= (TextView) findViewById(R.id.tv_yubao5);
		tv_yubao6	= (TextView) findViewById(R.id.tv_yubao6);
		tv_yubao7	= (TextView) findViewById(R.id.tv_yubao7);

		_max_tm1 = (TextView) findViewById(R.id._max_tm1);
		_min_tm1 = (TextView) findViewById(R.id._min_tm1);

		_max_tm2 = (TextView) findViewById(R.id._max_tm2);
		_min_tm2 = (TextView) findViewById(R.id._min_tm2);

		_max_tm3 = (TextView) findViewById(R.id._max_tm3);
		_min_tm3 = (TextView) findViewById(R.id._min_tm3);

		_max_tm4 = (TextView) findViewById(R.id._max_tm4);
		_min_tm4 = (TextView) findViewById(R.id._min_tm4);

		_max_tm5 = (TextView) findViewById(R.id._max_tm5);
		_min_tm5 = (TextView) findViewById(R.id._min_tm5);

		_max_tm6 = (TextView) findViewById(R.id._max_tm6);
		_min_tm6 = (TextView) findViewById(R.id._min_tm6);

		_max_tm7 = (TextView) findViewById(R.id._max_tm7);
		_min_tm7 = (TextView) findViewById(R.id._min_tm7);

		main_viewpager = (ViewPager) findViewById(R.id.main_viewpager);//滑动页面

		fourFragment = new ArrayList<Fragment>();
		fourFragment.add(new Fragment1());
		fourFragment.add(new Fragment2());
		fourFragment.add(new Fragment3());
		fourFragment.add(new Fragment4());

		fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fourFragment,myApplication);
		main_viewpager.setAdapter(fragmentAdapter);
		main_viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int witchView) {
				// TODO Auto-generated method stub
				switch (witchView) {
				case 0:
					myApplication.witchNum =1;
					if(myApplication.city1.equals("错误")  || myApplication.city1.equals(null)){
						tv_top.setText("");
						allInfoToBeFullOrNull();
					}else {
						tv_top.setText(myApplication.city1);
						getWeather(0);
					}
					break;
				case 1:
					myApplication.witchNum =2;
					if(myApplication.city2.equals("错误")  || myApplication.city2.equals(null)){
						tv_top.setText("");
						allInfoToBeFullOrNull();
					}else {
						tv_top.setText(myApplication.city2);
						getWeather(1);
					}
					break;
				case 2:
					myApplication.witchNum =3;
					if(myApplication.city3.equals("错误")  || myApplication.city3.equals(null)){
						tv_top.setText("");
						allInfoToBeFullOrNull();
					}else {
						tv_top.setText(myApplication.city3);
						getWeather(2);
					}
					break;
				case 3:
					myApplication.witchNum =4;
					if(myApplication.city4.equals("错误")  || myApplication.city4 .equals(null)){
						tv_top.setText("");
						allInfoToBeFullOrNull();
					}else {
						tv_top.setText(myApplication.city4);
						getWeather(3);
					}
					break;


				default:
					break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int witchView2) {
				// TODO Auto-generated method stub
				
			}
		});
		//=============================================================
		slidingMenu = new SlidingMenu(this);
		// 设置触摸屏幕的模式  
		slidingMenu.setMode(SlidingMenu.LEFT);//左侧划出
		slidingMenu.setTouchModeAbove(SlidingMenu.LEFT);// 设置滑动的屏幕范围，该设置为全屏区域都可以滑动
		slidingMenu.setBehindWidth(R.dimen.activity_music_option);// 菜单宽度
		slidingMenu.setFadeDegree(0.35f); 
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);  // 设置渐入渐出效果的值  
		slidingMenu.setMenu(R.layout.slide_menu);  //为侧滑菜单设置布局  
		slidingMenu.setBehindScrollScale(1.0f);
		slidingMenu.setShadowWidthRes(R.dimen.activity_music_option);  //阴影宽度
		// 设置滑动菜单视图的宽度  
		slidingMenu.setBehindOffsetRes(R.dimen.activity_music_option);
		slidingMenu.setOnCloseListener(new OnCloseListener() {
			@Override
			public void onClose() {
				// TODO Auto-generated method stub
				// 设置UI可以改变 自适应 本地歌曲搜索需要用到这个模式
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				//application.isSlidingMenuView = false;
			}
		});
		slidingMenu.setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {
				// TODO Auto-generated method stub
				// 调整输入模式 防止主页面UI被软键盘顶变形
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

			}
		});

		//侧滑菜单的绑定  ，只能写在侧滑菜单以后，不能写在前面，会空指针
		tv_sliding_current_city = (TextView) slidingMenu.findViewById(R.id.tv_sliding_current_city);
		tv_sliding_current_city.setOnClickListener(this);

		tv_sliding_set = (TextView) slidingMenu.findViewById(R.id.tv_sliding_set);
		tv_sliding_set.setOnClickListener(this);

		tv_sliding_more = (TextView) slidingMenu.findViewById(R.id.tv_sliding_more);
		tv_sliding_more.setOnClickListener(this);

		tv_sliding_exit = (TextView) slidingMenu.findViewById(R.id.tv_sliding_exit);
		tv_sliding_exit.setOnClickListener(this);
		//城市定位
		tv_sliding_now_city = (TextView) slidingMenu.findViewById(R.id.tv_sliding_now_city);
		
		startGPS();
		
		myApplication.witchNum =1;

		if(myApplication.city1.equals("错误")  || myApplication.city1 .equals(null)){
			tv_top.setText("");
			allInfoToBeFullOrNull();
		}else {
			tv_top.setText(myApplication.city1);
			getWeather(0);
		}
		
	//	tv_top.setText(myApplication.city1);
		System.out.println(myApplication.city1+"...................................................");
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		fragmentAdapter.notifyDataSetChanged();
		switch (myApplication.witchNum) {
		case 1:
			if(myApplication.city1.equals("错误")  || myApplication.city1 .equals(null)){
				tv_top.setText("");
				//allInfoToBeFullOrNull();
			}else {
				tv_top.setText(myApplication.city1);
				getWeather(0);
			}
			break;
	case 2:
		if(myApplication.city2.equals("错误")  || myApplication.city2 .equals(null)){
			tv_top.setText("");
			//allInfoToBeFullOrNull();
		}else {
			tv_top.setText(myApplication.city2);
			getWeather(1);
		}
			break;
	case 3:
		if(myApplication.city3.equals("错误")  || myApplication.city3 .equals(null)){
			tv_top.setText("");
			//allInfoToBeFullOrNull();
		}else {
			tv_top.setText(myApplication.city3);
			getWeather(2);
		}
		break;
	case 4:
		if(myApplication.city4.equals("错误")  || myApplication.city4 .equals(null)){
			tv_top.setText("");
			//allInfoToBeFullOrNull();
		}else {
			tv_top.setText(myApplication.city4);
			getWeather(3);
		}
		break;
		default:
			break;
		}
		//死循环代码
//		Intent intent = new Intent(WeatherMainActivity.this, WeatherMainActivity.class);
//		startActivity(intent);
//		finish();
			

	}
	public void startGPS() {
		// TODO Auto-generated method stub
		LocationManager locationManager;
		String serviceName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager)getSystemService(serviceName);
		String provider = LocationManager.NETWORK_PROVIDER;

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// String provider = locationManager.getBestProvider(criteria, true);

		Location location = locationManager.getLastKnownLocation(provider);
		updateWithNewLocation(location);
		locationManager.requestLocationUpdates(provider, 2000, 10,locationListener);
	}
	//实现刷新RefreshListener 中方法
	public void onRefresh(RefreshableView view) {
		//伪处理
		handler.sendEmptyMessageDelayed(1, 2000);
		Toast.makeText(mContext,"开始定位当前城市.......", Toast.LENGTH_SHORT).show();
		startGPS();//自己添加的方法
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
		switch (myApplication.witchNum) {
		case 1:
			if(myApplication.city1.equals("错误")  || myApplication.city1 .equals(null)){
				tv_top.setText("");
				//allInfoToBeFullOrNull();
			}else {
				tv_top.setText(myApplication.city1);
				getWeather(0);
			}
			break;
	case 2:
		if(myApplication.city2.equals("错误")  || myApplication.city2 .equals(null)){
			tv_top.setText("");
			//allInfoToBeFullOrNull();
		}else {
			tv_top.setText(myApplication.city2);
			getWeather(1);
		}
			break;
	case 3:
		if(myApplication.city3.equals("错误")  || myApplication.city3 .equals(null)){
			tv_top.setText("");
			//allInfoToBeFullOrNull();
		}else {
			tv_top.setText(myApplication.city3);
			getWeather(2);
		}
		break;
	case 4:
		if(myApplication.city4.equals("错误")  || myApplication.city4 .equals(null)){
			tv_top.setText("");
			//allInfoToBeFullOrNull();
		}else {
			tv_top.setText(myApplication.city4);
			getWeather(3);
		}
		break;
		default:
			break;
		}
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_main_pager_side:
			slidingMenu.showMenu();//显示菜单
			break;
		case R.id.tv_sliding_set://城市管理
//			WeatherMainActivity.this.
			finish();
			startActivity(new Intent(WeatherMainActivity.this,CityManager.class));
		
			break;		
			
		case R.id.tv_sliding_more:
			  AlertDialog.Builder builder = new Builder(WeatherMainActivity.this);
			 builder.setMessage("QQ2993103015，欢迎给出建议！");
			 builder.create().show();
			break;
			
		case R.id.tv_sliding_exit:
			finish();
			finish();
			finish();
			System.exit(0);
			break;

		default:
			break;

		}
	}
	public final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}
		public void onProviderDisabled(String provider){
			updateWithNewLocation(null);
		}
		public void onProviderEnabled(String provider){ }
		public void onStatusChanged(String provider, int status,
				Bundle extras){ }
	};
	public void updateWithNewLocation(Location location) {
		String dataForTop = null;
		String latLongString ;		
		if (location != null) {
			lat = location.getLatitude();//纬度
			lng = location.getLongitude();//经度
			latLongString ="纬度:"+ lat +"经度:"+ lng;
		} else {
			//latLongString ="无法获取地理信息";
		}
		List<Address> addList = null;
		Geocoder ge = new Geocoder(this);
		try {
			addList = ge.getFromLocation(lat, lng, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(addList!=null && addList.size()>0){
			for(int i=0; i<addList.size(); i++){
				Address ad = addList.get(i);
				// latLongString +="n";
				latLongString //+
				=// ad.getCountryName() +";"//经纬度
				//+ 
				ad.getLocality();//当前市区
				dataForTop = ad.getLocality();
			}
		}
//		tv_local_information.setText("您当前的位置是:"
			//	+ "纬度:"+ lat +";经度:" + lng  +"\n"      
	//			+latLongString);

		tv_sliding_now_city.setText("所在城市：娄底"/*+ dataForTop*/);
		
	}

	public void startTuChangeCity(int numFromFragment){

		switch (numFromFragment) {
		case 1:
			
			startActivity(new Intent(WeatherMainActivity.this,CityListActivity.class));
			break;
		case 2:
			
			startActivity(new Intent(WeatherMainActivity.this,CityListActivity.class));
			break;
		case 3:
			//myApplication.witchNum =3;
			startActivity(new Intent(WeatherMainActivity.this,CityListActivity.class));
			break;
		case 4:
			//myApplication.witchNum =4;	
			startActivity(new Intent(WeatherMainActivity.this,CityListActivity.class));
			break;

		default:
			break;
		}
	}
	public void getWeather(int witchCityArrylistNum){
		switch (witchCityArrylistNum) {
		case 0:
//			if(myApplication.city1.equals("错误")||myApplication.city1.equals(null))
//			{
//				allInfoToBeFullOrNull();
//				
//			}else{
				myApplication.activityCity = myApplication.cityDataArray1;
				arrayListToAllTextView();
//			}						
			break;
		case 1:
//			if(myApplication.city2.equals("错误")||myApplication.city2.equals(null))
//			{
//				allInfoToBeFullOrNull();
//				
//			}else{
			myApplication.activityCity =  myApplication.cityDataArray2;
			arrayListToAllTextView();
//			}
			break;
		case 2:
//			if(myApplication.city3.equals("错误")||myApplication.city3.equals(null))
//			{
//				allInfoToBeFullOrNull();			
//			}else{
			myApplication.activityCity =  myApplication.cityDataArray3;
			arrayListToAllTextView();
//			}
			break;
		case 3:
//			if(myApplication.city4.equals("错误")||myApplication.city4.equals(null))
//		{
//			allInfoToBeFullOrNull();
//			
//		}else{
			myApplication.activityCity =  myApplication.cityDataArray4;
			arrayListToAllTextView();
//		}
			break;


		default:
			break;
		}
	}
	
	public void arrayListToAllTextView(){
		
//		ArrayList<HashMap<String, Object>> ActivityArrayList = (ArrayList<HashMap<String, Object>>) myApplication.cityData3.get("weather");
		
		
		tv_yubao1.setText("周"+myApplication.activityCity.get(0).get("week").toString());
		tv_yubao2.setText("周"+myApplication.activityCity.get(1).get("week").toString());
		tv_yubao3.setText("周"+myApplication.activityCity.get(2).get("week").toString());
		tv_yubao4.setText("周"+myApplication.activityCity.get(3).get("week").toString());
		tv_yubao5.setText("周"+myApplication.activityCity.get(4).get("week").toString());
		tv_yubao6.setText("周"+myApplication.activityCity.get(5).get("week").toString());
		tv_yubao7.setText("周"+myApplication.activityCity.get(6).get("week").toString());


		tv_sun.setText("周"+myApplication.activityCity.get(0).get("week").toString());
		tv_mon.setText("周"+myApplication.activityCity.get(1).get("week").toString());
		tv_tus.setText("周"+myApplication.activityCity.get(2).get("week").toString());
		tv_wed.setText("周"+myApplication.activityCity.get(3).get("week").toString());
		tv_thus.setText("周"+myApplication.activityCity.get(4).get("week").toString());
		tv_fri.setText("周"+myApplication.activityCity.get(5).get("week").toString());
		tv_sat.setText("周"+myApplication.activityCity.get(6).get("week").toString());


		tv_sun1.setText(myApplication.activityCity.get(0).get("dayWindForADay").toString());
		tv_mon1.setText(myApplication.activityCity.get(1).get("dayWindForADay").toString());
		tv_tus1.setText(myApplication.activityCity.get(2).get("dayWindForADay").toString());
		tv_wed1.setText(myApplication.activityCity.get(3).get("dayWindForADay").toString());
		tv_thus1.setText(myApplication.activityCity.get(4).get("dayWindForADay").toString());
		tv_fri1.setText(myApplication.activityCity.get(5).get("dayWindForADay").toString());
		tv_sat1.setText(myApplication.activityCity.get(6).get("dayWindForADay").toString());


		img_yubao1.setImageResource(new NumToBeImage().NumToBeImageWay((myApplication.activityCity.get(0).get("dayImageNum").toString())));
		img_yubao2.setImageResource(new NumToBeImage().NumToBeImageWay((myApplication.activityCity.get(1).get("dayImageNum").toString())));
		img_yubao3.setImageResource(new NumToBeImage().NumToBeImageWay((myApplication.activityCity.get(2).get("dayImageNum").toString())));
		img_yubao4.setImageResource(new NumToBeImage().NumToBeImageWay((myApplication.activityCity.get(3).get("dayImageNum").toString())));
		img_yubao5.setImageResource(new NumToBeImage().NumToBeImageWay((myApplication.activityCity.get(4).get("dayImageNum").toString())));
		img_yubao6.setImageResource(new NumToBeImage().NumToBeImageWay((myApplication.activityCity.get(5).get("dayImageNum").toString())));
		img_yubao7.setImageResource(new NumToBeImage().NumToBeImageWay((myApplication.activityCity.get(6).get("dayImageNum").toString())));

		
		_max_tm1.setText(myApplication.activityCity.get(0).get("dayDushu").toString()+"℃");
		_min_tm1.setText(myApplication.activityCity.get(0).get("nightDushu").toString()+"℃");
		_max_tm2.setText(myApplication.activityCity.get(1).get("dayDushu").toString()+"℃");
		_min_tm2.setText(myApplication.activityCity.get(1).get("nightDushu").toString()+"℃");
		_max_tm3.setText(myApplication.activityCity.get(2).get("dayDushu").toString()+"℃");
		_min_tm3.setText(myApplication.activityCity.get(2).get("nightDushu").toString()+"℃");
		_max_tm4.setText(myApplication.activityCity.get(3).get("dayDushu").toString()+"℃");
		_min_tm4.setText(myApplication.activityCity.get(3).get("nightDushu").toString()+"℃");
		_max_tm5.setText(myApplication.activityCity.get(4).get("dayDushu").toString()+"℃");
		_min_tm5.setText(myApplication.activityCity.get(4).get("nightDushu").toString()+"℃");
		_max_tm6.setText(myApplication.activityCity.get(5).get("dayDushu").toString()+"℃");
		_min_tm6.setText(myApplication.activityCity.get(5).get("nightDushu").toString()+"℃");
		_max_tm7.setText(myApplication.activityCity.get(6).get("dayDushu").toString()+"℃");
		_min_tm7.setText(myApplication.activityCity.get(6).get("nightDushu").toString()+"℃");
		
		
		linearlayout_backgroud.setBackgroundResource(new NumToBeImage().changeBackgroud((myApplication.activityCity.get(0).get("dayImageNum").toString())));
		
	}
	public void allInfoToBeFullOrNull(){

		linearlayout_backgroud.setBackgroundResource(R.drawable.kong);//空背景
		
		tv_sun1.setText("");
		tv_mon1.setText("");
		tv_tus1.setText("");
		tv_wed1.setText("");
		tv_thus1.setText("");
		tv_fri1.setText("");
		tv_sat1.setText("");
		tv_sun.setText("");
		tv_mon.setText("");
		tv_tus.setText("");
		tv_wed.setText("");
		tv_thus.setText("");
		tv_fri.setText("");
		tv_sat.setText("");


		_max_tm1.setText("");
		_min_tm1.setText("");
		_max_tm2.setText("");
		_min_tm2.setText("");
		_max_tm3.setText("");
		_min_tm3.setText("");
		_max_tm4.setText("");
		_min_tm4.setText("");
		_max_tm5.setText("");
		_min_tm5.setText("");
		_max_tm6.setText("");
		_min_tm6.setText("");
		_max_tm7.setText("");
		_min_tm7.setText("");


		tv_yubao1.setText("");
		tv_yubao2.setText("");
		tv_yubao3.setText("");
		tv_yubao4.setText("");
		tv_yubao5.setText("");
		tv_yubao6.setText("");
		tv_yubao7.setText("");



		_max_tm1.setText("");
		_min_tm1.setText("");
		_max_tm2.setText("");
		_min_tm2.setText("");
		_max_tm3.setText("");
		_min_tm3.setText("");
		_max_tm4.setText("");
		_min_tm4.setText("");
		_max_tm5.setText("");
		_min_tm5.setText("");
		_max_tm6.setText("");
		_min_tm6.setText("");
		_max_tm7.setText("");
		_min_tm7.setText("");
	}
	

}
