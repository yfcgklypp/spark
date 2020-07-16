package com.example.utils.http;

import com.example.utils.logger.Log4jKit;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;

public class HttpUtils {

    public static String httpGet(String domain, String url) throws Exception {
        Log4jKit.info("HttpUtils.httpGet,  url===>>>{}", domain + url);
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
                .build();
        CloseableHttpResponse response = null;
        try {
            HttpGet httpget = new HttpGet(domain + url);
            httpget.addHeader("token",
                    "+JpRkCNZ1ilKweG7rQF14ul7VJ0xa9WMFBQjf7pxGVBpWrzf$NQ2E7dd9IpgQI9g");
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(entity);
				Log4jKit.info("HttpUtils.httpGet,  result===>>>{}", json);
                return json;
            }
        } catch (Exception e) {
            Log4jKit.error("http get请求统一处理异常", e);

        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    public static String httpPost(String domain, String url, String paramJson) throws Exception {
        Log4jKit.info("HttpUtils.httpPost,  url===>>>{}   paramJson===>>>{}", domain + url, paramJson);
        CloseableHttpClient httpclient = HttpClients.custom().build();
        StringEntity entity = new StringEntity(paramJson, "UTF-8");
        entity.setContentType("application/json");
        entity.setContentEncoding("UTF-8");
        HttpUriRequest post = RequestBuilder.post()
                .setUri(new URI(domain + url)).setEntity(entity)
                .build();
        post.setHeader("token", "+JpRkCNZ1ilKweG7rQF14ul7VJ0xa9WMFBQjf7pxGVBpWrzf$NQ2E7dd9IpgQI9g");
        CloseableHttpResponse response = httpclient.execute(post);
        try {
            HttpEntity responseEntity = response.getEntity();//中文乱码
            if (response.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(responseEntity);
				Log4jKit.info("HttpUtils.httpPost,  result===>>>{}", json);
                return json;
            }

        } catch (Exception e) {
            Log4jKit.error("http post请求统一处理异常", e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return response.getStatusLine().toString();
    }

}
