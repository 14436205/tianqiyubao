package cn.zjl.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


public class ToolTwo {

	private StringBuffer sb = new StringBuffer() ;
	public HashMap<String, Object> info(String city) {
		return json(httpClient_get(city));
	}
	public String httpClient_get(String city){
		final String url = "http://op.juhe.cn/onebox/weather/query?cityname=" + city + "&key=c0a40dd7b70c30c432a3d362cf3ffa5d" ;
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					//����HttpClient�ӿ�
					HttpClient client = new DefaultHttpClient() ;
					//ʹ��get���������ȡ
					HttpGet get = new HttpGet(url) ;
					//ִ��get��ȡ
					HttpResponse response = client.execute(get) ;
					//�ж��Ƿ����ӳɹ���200��ʾ���ӳɹ���
					if(response.getStatusLine().getStatusCode() == 200){
						//�����ӵ������ݻ�ȡ����
						HttpEntity entity = response.getEntity() ;
						InputStream input = entity.getContent() ;
						InputStreamReader reader = new InputStreamReader(input) ;
						BufferedReader br = new BufferedReader(reader) ;

						String line ;
						while ((line = br.readLine()) != null) {
							sb.append(new String(line)) ;

						}
						br.close();
						reader.close();
						input.close();

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}) ;

		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(sb.toString() != null){
			return sb.toString() ;
		}
		return null;

	}

	public HashMap<String, Object> json(String json) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		try {
			JSONObject job = new JSONObject(json);
			JSONObject js = job.getJSONObject("result");
			JSONObject data = js.getJSONObject("data");
			JSONObject realtime = data.getJSONObject("realtime");
			String time = realtime.getString("time");//发布时间
			String  curWeekNum = realtime.getString("week");//当前星期（数字数据）

			JSONObject windInfo = realtime.getJSONObject("wind");
			//将三者用“”+“，”+“”连接起来共用			
			String  windSpeed = windInfo.getString("windspeed");//当前风速
			String  winDirect = windInfo.getString("direct");//当前风向
			String  windPower = windInfo.getString("power");//当前风力
			
			
			
			JSONObject weathe = realtime.getJSONObject("weather");
			String temperature = weathe.getString("temperature");//当前温度
			String curWeatherText = weathe.getString("info");//当前天气一字流
			

			JSONObject pm = data.getJSONObject("pm25");
			JSONObject pm25 = pm.getJSONObject("pm25");
			String quality = pm25.getString("quality");//空气质量
			String pm252 = pm25.getString("pm25");//PM2.5
			String curPm = pm25.getString("curPm");//curPm

			
			
			
			
			JSONObject life = data.getJSONObject("life");
			JSONObject info = life.getJSONObject("info");

			JSONArray chuan = info.getJSONArray("chuanyi");

			String chuanyi = chuan.getString(0);
			JSONArray gan = info.getJSONArray("ganmao");	
			String ganmao = gan.getString(0);
			JSONArray zi = info.getJSONArray("ziwaixian");	
			String ziwaixian = zi.getString(0);
			JSONArray yun = info.getJSONArray("yundong");	
			String yundong = yun.getString(0);




			map.put("time", time);
			map.put("temperature", temperature);
			map.put("chuanyi", chuanyi);
			map.put("ganmao", ganmao);
			map.put("ziwaixian", ziwaixian);
			map.put("yundong", yundong);
			map.put("quality", quality);
			map.put("curWeekNum", curWeekNum);
			map.put("pm252", pm252);
			map.put("curPm", curPm);
			

			map.put("windSpeed", windSpeed);
			map.put("winDirect", winDirect);
			map.put("windPower", windPower);
			
			map.put("curWeatherText", curWeatherText);
			
			
			

			JSONArray weather = data.getJSONArray("weather");
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>() ;
			for (int i = 0; i < weather.length(); i++) {
				HashMap<String, Object> hash = new HashMap<String, Object>();
				JSONObject day = weather.getJSONObject(i);

				JSONObject dayInfo = day.getJSONObject("info");
				JSONArray arrayDay = dayInfo.getJSONArray("day");
				
				//接口中0代表了返回的天气图标
				String dayImageNum = arrayDay.getString(0);
				//白天天气，多云
				String dayQingkuang = arrayDay.getString(1);
				// 白天温度
				String dayDushu = arrayDay.getString(2);
				
				String dayWindForADay = arrayDay.getString(3);
				

				JSONArray arrayNight = dayInfo.getJSONArray("night");
				//接口中0代表了返回的天气图标
				String nightImageNum = arrayDay.getString(0);
				// 夜间天气，多云
				String nightQingkuang = arrayNight.getString(1);

				// 夜间温度
				String nightDushu = arrayNight.getString(2);

				// 当前星期，汉字数据
				String week = day.getString("week");

				hash.put("dayQingkuang", dayQingkuang) ;
				hash.put("dayDushu", dayDushu) ;
				hash.put("nightQingkuang", nightQingkuang) ;
				hash.put("nightDushu", nightDushu) ;
				hash.put("week", week) ;
				hash.put("dayImageNum", dayImageNum) ;
				hash.put("nightImageNum", nightImageNum) ;
				hash.put("dayWindForADay", dayWindForADay) ;

				
				
				
				list.add(hash) ;
			}
			map.put("weather", list);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return map;
	}
}
