package com.smx.util;

import com.smx.App;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class JsonUtil {
	public static final String USER_ID_KEY = "user_id";
	public static final String CHANNEL_ID_KEY = "channel_id";
	
	public static final String USERNAME_KEY = "username";
//	public static final String PORTRAIT_KEY = "portrait";
	public static final String MESSAGE_KEY = "message";
	public static final String TIMESTAMP_KEY = "timestamp";
	public static final String TAG_KEY = "tag";
	
	public static String createJsonMsg(long timeSamp, String message/*, String tag*/) {
		SharePreferenceUtil util = App.getInstance().getSpUtil();
		try {
			String result = new JSONStringer().object()
					.key(USER_ID_KEY).value(util.getUserId())
					.key(CHANNEL_ID_KEY).value(util.getChannelId())
					.key(USERNAME_KEY).value(util.getUsername())
//					.key(PORTRAIT_KEY).value(util.getPortrait())
					.key(MESSAGE_KEY).value(message)
					.key(TIMESTAMP_KEY).value(timeSamp)
//					.key(TAG_KEY).value(tag)
					.endObject().toString();
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String createJsonMsg(long timeSamp, String message, String tag) {
		SharePreferenceUtil util = App.getInstance().getSpUtil();
		try {
			String result = new JSONStringer().object()
					.key(USER_ID_KEY).value(util.getUserId())
					.key(CHANNEL_ID_KEY).value(util.getChannelId())
					.key(USERNAME_KEY).value(util.getUsername())
//					.key(PORTRAIT_KEY).value(util.getPortrait())
					.key(MESSAGE_KEY).value(message)
					.key(TIMESTAMP_KEY).value(timeSamp)
					.key(TAG_KEY).value(tag)
					.endObject().toString();
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static JSONObject getJSONObject(String content) {
		JSONObject jsonContent = null;
		try {
			jsonContent = new JSONObject(content);
		} catch (JSONException e) {
			// e.printStackTrace();
			Logger.e("Parse bind json infos error: " + e);
		}
		return jsonContent;
	}

	/**
	 * 是不是自己的消息
	 * 
	 * @param msg
	 * @return
	 */
	public static boolean isMe(String msg) {
		boolean isMe = false;
		try {
			JSONObject jsonContent = getJSONObject(msg);
			isMe = jsonContent.getString(USER_ID_KEY).equals(
					App.getInstance().getSpUtil().getUsername());
		} catch (JSONException e) {
			// e.printStackTrace();
			Logger.e("Parse bind json infos error: " + e);
		}
		return isMe;
	}

	/**
	 * 从json消息中提取消息内容
	 * 
	 * @param msg
	 * @return
	 */
	public static String getUserId(String msg){
		String userId = "";
		try {
			JSONObject jsonContent = new JSONObject(msg);
			userId = jsonContent.getString(USER_ID_KEY);
		} catch (JSONException e) {
			// e.printStackTrace();
			Logger.e("Parse bind json infos error: " + e);
		}
		return userId;
	}
	
	public static String getChannelId(String msg){
		String channelId = "";
		try {
			JSONObject jsonContent = new JSONObject(msg);
			channelId = jsonContent.getString(CHANNEL_ID_KEY);
		} catch (JSONException e) {
			// e.printStackTrace();
			Logger.e("Parse bind json infos error: " + e);
		}
		return channelId;
	}
	
	public static String getUsername(String msg){
		String nick = "";
		try {
			JSONObject jsonContent = new JSONObject(msg);
			nick = jsonContent.getString(USERNAME_KEY);
		} catch (JSONException e) {
			// e.printStackTrace();
			Logger.e("Parse bind json infos error: " + e);
		}
		return nick;
	}
	
	/*public static String getPortrait(String msg){
		String userHead = "";
		try {
			JSONObject jsonContent = new JSONObject(msg);
			userHead = jsonContent.getString(PORTRAIT_KEY);
		} catch (JSONException e) {
			// e.printStackTrace();
			Logger.e("Parse bind json infos error: " + e);
		}
		return userHead;
	}*/
	
	public static String getMessage(String msg) {
		String message = "";
		try {
			JSONObject jsonContent = new JSONObject(msg);
			message = jsonContent.getString(MESSAGE_KEY);
		} catch (JSONException e) {
			// e.printStackTrace();
			Logger.e("Parse bind json infos error: " + e);
		}
		return message;
	}
	
	public static String getTag(String msg){
		String tag = "";
		try {
			JSONObject jsonContent = new JSONObject(msg);
			tag = jsonContent.getString(TAG_KEY);
		} catch (JSONException e) {
			// e.printStackTrace();
			Logger.e("Parse bind json infos error: " + e);
		}
		return tag;
	}
	
	
	
	
	
	
	
	
	public static String fixJson(String result){
		 result = result.replace("\\\\\\\\\\\\\\\\", "\\\\");
		 result = result.replace("\\\\\\\"", "\"");
		 result = result.replace("\\\"", "\"");
		 result = result.replace("\\\\\\\\\\\\\\/", "/");
		 result = result.replace("\\", "");
	     result = result.substring(1);
	     result = result.substring(0, result.length() - 1);
	     result = result.replace("\"[", "[");
	     result = result.replace("]\"", "]");
	     
	     System.out.println(result);
	     return result;
	}
	
	public static String fixBinaryJson(String result){
		result = result.replace("<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">", "");
		result = result.replace("</string>", "");
		
		System.out.println(result);
		return result;
	}
}
