package weatherService;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class weatherServiceTest {

	public static String searchWeather(String proviceName,String cityName,String placeName) {
		String cityid=weatherInfo.cityId(proviceName,cityName,placeName);
		
		
		String host = "http://freecityid.market.alicloudapi.com";
		String path = "/whapi/json/alicityweather/briefforecast3days";
		String method = "POST";
		String appcode = "4651f4171ccf47bfb8ca9b5fa12137b6";
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + appcode);
		// 根据API的要求，定义相对应的Content-Type
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		Map<String, String> querys = new HashMap<String, String>();
		Map<String, String> bodys = new HashMap<String, String>();
		bodys.put("cityId", cityid);
		bodys.put("token", "677282c2f1b3d718152c4e25ed434bc4");

		try {
			/**
			 * 重要提示如下: HttpUtils请从
			 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
			 * 下载
			 *
			 * 相应的依赖请参照
			 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
			 */
			HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
//	    	System.out.println(response.toString());
			// 获取response的body
//	    	System.out.println(EntityUtils.toString(response.getEntity()));
			String info = EntityUtils.toString(response.getEntity());
			
			return info;
			
			
		

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
