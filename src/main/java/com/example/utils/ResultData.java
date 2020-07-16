package com.example.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ResultData<T> {

	private boolean success = true;

	private int code = HttpStatus.SC_OK;

	private String message;

	private Long totalCount;

	private Integer page;

	private Integer pageSize;
	
	private T		data;
	
	public static ResultData getResult(int code, String message, Object data) {
		ResultData result = new ResultData();
		result.code = code;
		result.message = message;
		result.data = data;
		return result;
	}
	
	public static String fail(String message, int code) {
		
		String str = "{}";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		map.put("message", message);
		try {
			str = JSONObject.toJSONString(map);
		} catch (Exception e) {
		}
		
		return str;
	}
	
	public static ResultData getResult(Object data) {
		return getResult(200, "成功", data);
	}
	
	public static ResultData getResult(String message, Object data) {
		return getResult(200, message, data);
	}
	
	public static ResultData getResultError(String message) {
		return getResult(600, message, null);
	}
	
	public static ResultData getResultSuccess(String message) {
		return getResult(200, message, null);
	}
	
	public static ResultData getSuccess() {
		return getResult(200, "成功", null);
	}
	
	/**
	 * @return
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
}
