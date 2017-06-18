package cn.zjl.city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;







import cn.zjl.application.MyApplication;
import cn.zjl.citylist.widget.ContactItemInterface;
import cn.zjl.citylist.widget.ContactListViewImpl;
import cn.zjl.example.androidweather.R;
import cn.zjl.sharedpreferences.SharedPreferencesUtils;
import cn.zjl.tools.ToolTwo;




import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;



public class CityListActivity extends Activity implements TextWatcher{

	//自修改
	protected Context mContext;
	MyApplication myApplication;
	ToolTwo toolTwo;


	private Context context_ = CityListActivity.this;

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
		setContentView(R.layout.citylist);
		//自修改
		mContext = CityListActivity.this;
		myApplication = (MyApplication) getApplication();


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

				switch (myApplication.witchNum) {
				case 1:
					toolTwo = new ToolTwo();					
					myApplication.city1 = 	searchList.get(position).getDisplayInfo();
					SharedPreferencesUtils.inputValue(mContext, "city1", searchList.get(position).getDisplayInfo());
					myApplication.cityData1 = 	toolTwo.info(myApplication.city1);
					myApplication.cityDataArray1 = (ArrayList<HashMap<String, Object>>) myApplication.cityData1.get("weather");
					finish();
					break;
				case 2:
					toolTwo = new ToolTwo();
					myApplication.city2 = 	searchList.get(position).getDisplayInfo();
					SharedPreferencesUtils.inputValue(mContext, "city2", searchList.get(position).getDisplayInfo());
					myApplication.cityData2 = 	toolTwo.info(myApplication.city2);
					myApplication.cityDataArray2 = (ArrayList<HashMap<String, Object>>) myApplication.cityData2.get("weather");
					finish();
					break;
				case 3:
					toolTwo = new ToolTwo();
					myApplication.city3 = 	searchList.get(position).getDisplayInfo();
					SharedPreferencesUtils.inputValue(mContext, "city3", searchList.get(position).getDisplayInfo());
					myApplication.cityData3 = 	toolTwo.info(myApplication.city3);
					myApplication.cityDataArray3 = (ArrayList<HashMap<String, Object>>) myApplication.cityData3.get("weather");
					finish();
					break;
				case 4:
					toolTwo = new ToolTwo();
					myApplication.city4 = 	searchList.get(position).getDisplayInfo();
					SharedPreferencesUtils.inputValue(mContext, "city4", searchList.get(position).getDisplayInfo());
					myApplication.cityData4 = 	toolTwo.info(myApplication.city4);
					myApplication.cityDataArray4 = (ArrayList<HashMap<String, Object>>) myApplication.cityData4.get("weather");
					finish();
					break;
				default:
					break;
				}

			}
		});

		searchBox = (EditText) findViewById(R.id.input_search_query);
		searchBox.addTextChangedListener(this);
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

}
