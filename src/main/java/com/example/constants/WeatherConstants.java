package com.example.constants;

/**
 * @ClassName WeatherConstants天气
 * @Author yupanpan
 * @Date 2020/6/18 14:56
 */
public class WeatherConstants {

    public static String appid="xxxxxx";
    public static String appsecret="xxxxxxx";

    //专业七日天气接口
    public static String weatherOfSevenDay="https://yiketianqi.com/api";

    public enum Version{
        V1("v1",""),V2("v2",""),V3("v3",""),
        V4("v4",""),V5("v5",""),V6("v6",""),
        V9("v9",""),;
        private String number;
        private String desc;

        Version(String number, String desc) {
            this.number = number;
            this.desc = desc;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public static String getCompleteWeatherOfSevenDayUrl(){
        return weatherOfSevenDay+"?appid="+appid+"&appsecret="+appsecret+"&version="+Version.V9.getNumber();
    }
}
