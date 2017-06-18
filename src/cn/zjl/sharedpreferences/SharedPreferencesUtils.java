package cn.zjl.sharedpreferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

//xml 存储工具类
public class SharedPreferencesUtils {

	private static SharedPreferences preferences;
	private static Editor editor;

	//存数据
	public static void inputValue(Context context, String key, Object value) {
		
		preferences = context.getSharedPreferences("FourCitiesName",//表名？
				Activity.MODE_PRIVATE);
		editor = preferences.edit();
		if (value instanceof Integer) {
			editor.putInt(key, (Integer) value);
		} else if (value instanceof String) {
			editor.putString(key, (String) value);
		} else if (value instanceof Float) {
			editor.putFloat(key, (Float) value);
		} else if (value instanceof Boolean) {
			editor.putBoolean(key, (Boolean) value);
		} else if (value instanceof Long) {
			editor.putLong(key, (Long) value);
		}
		editor.commit();
	}

	//取数据
	public static SharedPreferences outputValue(Context context) {
		preferences = context.getSharedPreferences("FourCitiesName", Activity.MODE_PRIVATE);
		return preferences;
	}
	/**
	 * 取出是否加载介绍页面第一次安装的判断值
	 * @param context
	 * @param key键
	 * @param value值
	 * @param fileName表名
	 * @return
	 */
	public static Object outputValueTwo(Context context,String fileName,String key,Object value){
		preferences=context.getSharedPreferences(fileName,Activity.MODE_PRIVATE);
//Object value是用于判断哪种类型的值，然后进行筛选，比如return preferences.getString(key, "错误" );中，报错则返回：错误.....fileName表名
		if(value instanceof Integer){
			return preferences.getInt(key,0);
		}else if(value instanceof String){
			return preferences.getString(key, "错误" );
		}else if(value instanceof Boolean){
			return preferences.getBoolean(key, false);
		}else if(value instanceof Float){
			return preferences.getFloat(key,0);
		}else if(value instanceof Long){
			return preferences.getLong(key,0);
		}else{
			return null;
		}
	}
}
