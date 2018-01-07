package com.smx;

import java.io.InputStream;
import java.util.Properties;

public class Configuration {
	public static String ws_url;
	public static String appVersion;
	public static String databaseName;
	public static int databaseVersion;
	public static int connectTimeout;
	public static int readTimeout;
	
	static {
		try {
			InputStream in = Configuration.class.getResourceAsStream("/assets/system.properties");
			/**
			 * ��ʹ��
			 *
			 * InputStream in = context.getResources().getAssets().open("system.properties");
			 */

			/**
			 * ������
			 *
			 * Return a global shared Resources object that provides access to
			 * only system resources (no application resources), and is not configured for
			 * the current screen (can not use dimension units, does not change based on orientation, etc).
			 *
			 * InputStream in = Resources.getSystem().getAssets().open(fileName);
			 */
			Properties properties = new Properties();
			properties.load(in);

			ws_url = properties.getProperty("WS_URL");
			appVersion = properties.getProperty("APP_VERSION");
			databaseName = properties.getProperty("DATABASE_NAME");
			databaseVersion = Integer.valueOf(properties.getProperty("DATABASE_VERSION"));
			connectTimeout = Integer.valueOf(properties.getProperty("CONNECT_TIME_OUT"));
			readTimeout = Integer.valueOf(properties.getProperty("READ_TIME_OUT"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
