package com.example.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.constants.WeatherConstants;
import com.example.entity.weather.HourInfo;
import com.example.utils.http.HttpUtils;
import com.example.utils.mail.MailUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName WeatherTask
 * @Author yupanpan
 * @Date 2020/6/18 13:55
 */
@Component
public class WeatherTask {

    @Scheduled(cron = "0 0 7 1/1 * ?")
    public void sendWeather() throws Exception {
        String weatherOfSevenDayUrl = WeatherConstants.getCompleteWeatherOfSevenDayUrl();
        weatherOfSevenDayUrl+="&cityid=101270101";
        String result = HttpUtils.httpGet("", weatherOfSevenDayUrl);
        StringBuilder sb=new StringBuilder();
        JSONObject resultData = JSONObject.parseObject(result);
        //城市名称
        String city = resultData.get("city").toString();
        sb.append(city).append("。");

        JSONArray datas = resultData.getJSONArray("data");
        //一共7天，只取第一天(当天)
        JSONObject data = JSONObject.parseObject(datas.get(0).toString());
        //时间
        String date = data.getString("date");
        sb.append(date).append("(");
        String week = data.getString("week");
        sb.append(week).append(")").append("，");
        //天气情况
        String wea = data.get("wea").toString();
        sb.append("天气情况:").append(wea).append("，");
        //实时温度
        String tem = data.get("tem").toString();
        sb.append("实时温度:").append(tem).append("，");
        //最高温
        String tem1 = data.get("tem1").toString();
        sb.append("最高温:").append(tem1).append("，");
        //最低温
        String tem2 = data.get("tem2").toString();
        sb.append("最低温:").append(tem2).append("，");
        //空气质量
        String air = data.get("air").toString();
        sb.append("空气质量:").append(air).append(",");
        //空气质量等级
        String airLevel = data.get("air_level").toString();
        sb.append("空气质量等级:").append(airLevel).append(",");
        //空气质量提示
        String airTips = data.get("air_tips").toString();
        sb.append(airTips).append("。");

        //生活指数(6种)
        JSONArray index = data.getJSONArray("index");
        for (Object o : index) {
            JSONObject lifeObject = JSONObject.parseObject(o.toString());
            //穿衣指数等指数类型
            String title = lifeObject.get("title").toString();
            sb.append(title).append(",");
            //指数级别
            String level = lifeObject.get("level").toString();
            sb.append(level).append(",");
            //描述
            String desc = lifeObject.get("desc").toString();
            sb.append(desc);
        }

        List<HourInfo> hours = JSONObject.parseArray(data.get("hours").toString(), HourInfo.class);
        for (HourInfo hour : hours) {
            sb.append(hour.getHours()).append(":").append(hour.getWea()).append(",").append(hour.getTem()).append(",").append(hour.getWin()).append("。");
        }
        MailUtils.sendSimpleMail(sb.toString(),"天气预报","15928878437@163.com");
    }
}
