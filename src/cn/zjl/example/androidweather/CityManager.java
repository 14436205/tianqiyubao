package cn.zjl.example.androidweather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.zjl.application.MyApplication;
import cn.zjl.city.CityAdapter;
import cn.zjl.city.CityData;
import cn.zjl.city.CityItem;
import cn.zjl.citylist.widget.ContactItemInterface;
import cn.zjl.citylist.widget.ContactListViewImpl;
import cn.zjl.example.androidweather.R;
import cn.zjl.sharedpreferences.SharedPreferencesUtils;
import cn.zjl.tools.ToolTwo;


public class CityManager extends Activity implements TextWatcher,OnClickListener{

	//自修改
	protected Context mContext;
	MyApplication myApplication;
	ToolTwo toolTwo;

	LinearLayout linear_include;
	TextView   tv_manager_city1,tv_manager_city2,tv_manager_city3,tv_manager_city4;
	public int whitchManager; 


	private Context context_ = CityManager.this;

	private ContactListViewImpl listview;

	private EditText searchBox;
	private String searchString;

	private Object searchLock = new Object();
	boolean inSearchMode = false;

	private final static String TAG = "MainActivity2";

	List<ContactItemInterface> contactList;
	List<ContactItemInterface> filterList;
	private SearchListTask curSearchTask = null;



	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_manager);
		//自修改
		mContext = CityManager.this;
		myApplication = (MyApplication) getApplication();

		linear_include = (LinearLayout) findViewById(R.id.linear_include);
		tv_manager_city1 = (TextView) findViewById(R.id.tv_manager_city1);
		tv_manager_city1.setOnClickListener(this);
		tv_manager_city2 = (TextView) findViewById(R.id.tv_manager_city2);
		tv_manager_city2.setOnClickListener(this);
		tv_manager_city3 = (TextView) findViewById(R.id.tv_manager_city3);
		tv_manager_city3.setOnClickListener(this);
		tv_manager_city4 = (TextView) findViewById(R.id.tv_manager_city4);
		tv_manager_city4.setOnClickListener(this);

		linear_include.setVisibility(linear_include.INVISIBLE);

		filterList = new ArrayList<ContactItemInterface>();
		contactList = CityData.getSampleContactList();

		CityAdapter adapter = new CityAdapter(this,R.layout.city_item, contactList);

		listview = (ContactListViewImpl) this.findViewById(R.id.listview);
		listview.setFastScrollEnabled(true);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView parent, View v, int position,long id)
			{
				List<ContactItemInterface> searchList = inSearchMode ? filterList: contactList;
				Toast.makeText(context_,searchList.get(position).getDisplayInfo(),Toast.LENGTH_SHORT).show();
				switch (whitchManager) {
				case 1:
					toolTwo = new ToolTwo();					
					myApplication.city1 = 	searchList.get(position).getDisplayInfo();
					SharedPreferencesUtils.inputValue(mContext, "city1", searchList.get(position).getDisplayInfo());
					myApplication.cityData1 = 	toolTwo.info(myApplication.city1);
					myApplication.cityDataArray1 = (ArrayList<HashMap<String, Object>>) myApplication.cityData1.get("weather");
					tv_manager_city1.setText(searchList.get(position).getDisplayInfo());
					linear_include.setVisibility(linear_include.INVISIBLE);
					break;
				case 2:
					toolTwo = new ToolTwo();
					myApplication.city2 = 	searchList.get(position).getDisplayInfo();
					SharedPreferencesUtils.inputValue(mContext, "city2", searchList.get(position).getDisplayInfo());
					myApplication.cityData2 = 	toolTwo.info(myApplication.city2);
					myApplication.cityDataArray2 = (ArrayList<HashMap<String, Object>>) myApplication.cityData2.get("weather");
					tv_manager_city2.setText(searchList.get(position).getDisplayInfo());
					linear_include.setVisibility(linear_include.INVISIBLE);
					break;
				case 3:
					toolTwo = new ToolTwo();
					myApplication.city3 = 	searchList.get(position).getDisplayInfo();
					SharedPreferencesUtils.inputValue(mContext, "city3", searchList.get(position).getDisplayInfo());
					myApplication.cityData3 = 	toolTwo.info(myApplication.city3);
					myApplication.cityDataArray3 = (ArrayList<HashMap<String, Object>>) myApplication.cityData3.get("weather");
					tv_manager_city3.setText(searchList.get(position).getDisplayInfo());
					linear_include.setVisibility(linear_include.INVISIBLE);
					break;
				case 4:
					toolTwo = new ToolTwo();
					myApplication.city4 = 	searchList.get(position).getDisplayInfo();
					SharedPreferencesUtils.inputValue(mContext, "city4", searchList.get(position).getDisplayInfo());
					myApplication.cityData4 = 	toolTwo.info(myApplication.city4);
					myApplication.cityDataArray4 = (ArrayList<HashMap<String, Object>>) myApplication.cityData4.get("weather");
					tv_manager_city4.setText(searchList.get(position).getDisplayInfo());
					linear_include.setVisibility(linear_include.INVISIBLE);
					break;
				default:
					break;
				}

			}
		});

		searchBox = (EditText) findViewById(R.id.input_search_query);
		searchBox.addTextChangedListener(this);
		
		
		
		if(myApplication.city1.equals("错误")  || myApplication.city1 .equals(null)){
			tv_manager_city1.setText("");
		
		}else {
			tv_manager_city1.setText(myApplication.city1);
		}
		if(myApplication.city2.equals("错误")  || myApplication.city2.equals(null)){
			tv_manager_city2.setText("");
		
		}else {
			tv_manager_city2.setText(myApplication.city2);
		}
		if(myApplication.city3.equals("错误")  || myApplication.city3 .equals(null)){
			tv_manager_city3.setText("");
		
		}else {
			tv_manager_city3.setText(myApplication.city3);
		}
		if(myApplication.city4.equals("错误")  || myApplication.city4.equals(null)){
			tv_manager_city4.setText("");
		
		}else {
			tv_manager_city4.setText(myApplication.city4);
		}
	}

	@Override
	public void afterTextChanged(Editable s)
	{
		searchString = searchBox.getText().toString().trim().toUpperCase();

		if (curSearchTask != null
				&& curSearchTask.getStatus() != AsyncTask.Status.FINISHED)
		{
			try
			{
				curSearchTask.cancel(true);
			} catch (Exception e)
			{
				Log.i(TAG, "Fail to cancel running search task");
			}

		}
		curSearchTask = new SearchListTask();
		curSearchTask.execute(searchString); 
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after)
	{
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		// do nothing
	}

	private class SearchListTask extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params)
		{
			filterList.clear();

			String keyword = params[0];

			inSearchMode = (keyword.length() > 0);

			if (inSearchMode)
			{
				// get all the items matching this
				for (ContactItemInterface item : contactList)
				{
					CityItem contact = (CityItem) item;

					boolean isPinyin = contact.getFullName().toUpperCase().indexOf(keyword) > -1;
					boolean isChinese = contact.getNickName().indexOf(keyword) > -1;

					if (isPinyin || isChinese)
					{
						filterList.add(item);
					}

				}

			}
			return null;
		}

		protected void onPostExecute(String result)
		{

			synchronized (searchLock)
			{

				if (inSearchMode)
				{

					CityAdapter adapter = new CityAdapter(context_,R.layout.city_item, filterList);
					adapter.setInSearchMode(true);
					listview.setInSearchMode(true);
					listview.setAdapter(adapter);
				} else
				{
					CityAdapter adapter = new CityAdapter(context_,R.layout.city_item, contactList);
					adapter.setInSearchMode(false);
					listview.setInSearchMode(false);
					listview.setAdapter(adapter);
				}
			}

		}
	}





	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		startActivity(new Intent(CityManager.this,WeatherMainActivity.class));
		CityManager.this.finish();

		return super.onKeyDown(keyCode, event);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_manager_city1:
			whitchManager =1;
			linear_include.setVisibility(linear_include.VISIBLE);
			break;
		case R.id.tv_manager_city2:
			whitchManager =2;
			linear_include.setVisibility(linear_include.VISIBLE);
			break;

		case R.id.tv_manager_city3:
			whitchManager = 3;
			linear_include.setVisibility(linear_include.VISIBLE);
			break;
		case R.id.tv_manager_city4:
			whitchManager = 4;
			linear_include.setVisibility(linear_include.VISIBLE);
			break;

		default:
			break;
		}
	}
}
