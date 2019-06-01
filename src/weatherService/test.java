package weatherService;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class test {

	public static void main(String[] args) {
		JSONObject oneDayInfo = null;
		JSONObject twoDayInfo = null;
		JSONObject threeDayInfo = null;

		String info = weatherServiceTest.searchWeather("湖北省", "襄阳市", "襄城区");
		JSONObject infoJson = JSONObject.parseObject(info);
		System.out.println(infoJson.toJSONString());

		JSONObject dataInfo = JSONObject.parseObject(infoJson.get("data").toString());
		JSONObject cityInfo = JSONObject.parseObject(dataInfo.get("city").toString());
		JSONArray forecastInfo = JSONArray.parseArray(dataInfo.get("forecast").toString());
		for (int i = 0; i < forecastInfo.size(); i++) {
			if (i == 0) {
				oneDayInfo = (JSONObject) forecastInfo.get(i);
			} else if (i == 1) {
				twoDayInfo = (JSONObject) forecastInfo.get(i);
			} else if (i == 2) {
				threeDayInfo = (JSONObject) forecastInfo.get(i);
			}

		}

		StringBuffer weatherInfo = new StringBuffer();
		weatherInfo.append("您要查询的天气情况如下:\n");
		weatherInfo.append("省份:" + cityInfo.get("pname") + ",城市:" + cityInfo.get("secondaryname") + ",地区:"
				+ cityInfo.get("name") + "\n");
		weatherInfo.append("今日:\n" + "白天天气:" + oneDayInfo.get("conditionDay") + ",夜间天气:" + oneDayInfo.get("conditionNight")
				+ ",湿度:" + oneDayInfo.get("humidity") + ",白天温度:" + oneDayInfo.get("tempDay") + ",夜间温度:"
				+ oneDayInfo.get("tempNight") + ",预报日期:" + oneDayInfo.get("predictDate") + ",白天风向:"
				+ oneDayInfo.get("windDirDay") + ",白天风力:" + oneDayInfo.get("windLevelDay") + ",夜间风向:"
				+ oneDayInfo.get("windDirNight") + ",夜间风力:" + oneDayInfo.get("windLevelNight") + "\n");
		weatherInfo.append("未来三日天气预报如下:\n");
		weatherInfo.append("明天:\n" + "白天天气:" + twoDayInfo.get("conditionDay") + ",夜间天气:" + twoDayInfo.get("conditionNight")
		+ ",湿度:" + twoDayInfo.get("humidity") + ",白天温度:" + twoDayInfo.get("tempDay") + ",夜间温度:"
		+ twoDayInfo.get("tempNight") + ",预报日期:" + twoDayInfo.get("predictDate") + ",白天风向:"
		+ twoDayInfo.get("windDirDay") + ",白天风力:" + twoDayInfo.get("windLevelDay") + ",夜间风向:"
		+ twoDayInfo.get("windDirNight") + ",夜间风力:" + twoDayInfo.get("windLevelNight") + "\n");
		
		weatherInfo.append("后天:\n" + "白天天气:" + threeDayInfo.get("conditionDay") + ",夜间天气:" + threeDayInfo.get("conditionNight")
		+ ",湿度:" + threeDayInfo.get("humidity") + ",白天温度:" + threeDayInfo.get("tempDay") + ",夜间温度:"
		+ threeDayInfo.get("tempNight") + ",预报日期:" + threeDayInfo.get("predictDate") + ",白天风向:"
		+ threeDayInfo.get("windDirDay") + ",白天风力:" + threeDayInfo.get("windLevelDay") + ",夜间风向:"
		+ threeDayInfo.get("windDirNight") + ",夜间风力:" + threeDayInfo.get("windLevelNight") + "\n");


		System.out.println(weatherInfo.toString());

	}

}
