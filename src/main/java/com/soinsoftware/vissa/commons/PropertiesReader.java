package com.soinsoftware.vissa.commons;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertiesReader {
	private static PropertiesReader instance = new PropertiesReader();
	private Properties properties = new Properties();

	private List<String> propertiesLoaded = new ArrayList<String>();

	public static PropertiesReader getInstance() {
		if (instance == null) {
			instance = new PropertiesReader();
		}
		return instance;
	}

	public void loadProperties(String propertiesFile) throws IOException {
		if (this.propertiesLoaded.contains(propertiesFile)) {
			return;
		}
		InputStream propertiesStream = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			propertiesStream = classLoader.getResourceAsStream(propertiesFile);
			if (propertiesStream == null) {
				propertiesStream = new FileInputStream(propertiesFile);
			}
			this.properties.load(propertiesStream);
			this.propertiesLoaded.add(propertiesFile);
		} finally {
			if (propertiesStream != null) {
				propertiesStream.close();
			}
		}
	}

	public Integer getIntProperty(String propertyName) {
		String value = this.properties.getProperty(propertyName);
		if (value != null) {
			return Integer.valueOf(value);
		}
		return null;
	}

	public Integer getIntProperty(String propertyName, int defaultValue) {
		String value = this.properties.getProperty(propertyName);
		if (value != null) {
			return Integer.valueOf(value);
		}
		return Integer.valueOf(defaultValue);
	}

	public String getTextProperty(String propertyName) {
		return this.properties.getProperty(propertyName);
	}

	public String getTextProperty(String propertyName, String defaultValue) {
		String propertyValue = this.properties.getProperty(propertyName);
		if (propertyValue == null) {
			return defaultValue;
		}
		return propertyValue;
	}

	public Boolean getBooleanProperty(String propertyName) {
		String value = this.properties.getProperty(propertyName);
		if (value != null) {
			return Boolean.valueOf(value);
		}
		return null;
	}

	public Boolean getBooleanProperty(String propertyName, boolean defaultValue) {
		String value = this.properties.getProperty(propertyName);
		if (value != null) {
			return Boolean.valueOf(value);
		}
		return Boolean.valueOf(defaultValue);
	}
}
