package com.soinsoftware.vissa.commons;

import org.apache.log4j.Logger;

public class CommonsConstants {

	protected static final Logger log = Logger.getLogger(CommonsConstants.class);

	private static final String PROPERTY_FILE_PATH = "C:\\Users\\carlosandres\\git\\vissa-notifications\\src\\main\\resources\\vissa-notification.properties";

	public static String FORMAT_DATE_TIME;
	public static String FORMAT_DATE;
	public static String SMS_ACCOUNT_ID;
	public static String SMS_AUTH_TOKEN;
	public static String SMS_PHONES_TO;
	public static String SMS_PHONES_FROM;
	public static Integer EXPIRATION_DAYS;
	public static String MESSAGE_EXPIRATION;

	static {
		if (!reloadProperties()) {
			throw new ExceptionInInitializerError("Error al inicializar archivo de propiedades: " + PROPERTY_FILE_PATH);
		}
	}

	/*
	 * Cargar propiedades del archivo de propiedades
	 *
	 */
	public static boolean reloadProperties() {
		try {

			PropertiesReader properties = PropertiesReader.getInstance();
			properties.loadProperties(PROPERTY_FILE_PATH);

			FORMAT_DATE_TIME = properties.getTextProperty("format.date.time");
			FORMAT_DATE = properties.getTextProperty("format.date");
			SMS_ACCOUNT_ID = properties.getTextProperty("sms.account.id");
			SMS_AUTH_TOKEN = properties.getTextProperty("sms.auth.token");
			SMS_PHONES_TO = properties.getTextProperty("sms.phones.to");
			SMS_PHONES_FROM = properties.getTextProperty("sms.phones.from");
			String days = properties.getTextProperty("expiration.days").trim();
			EXPIRATION_DAYS = days != null && !days.isEmpty() ? Integer.parseInt(days) : null;
			MESSAGE_EXPIRATION = properties.getTextProperty("message.expiration");

		} catch (Exception e) {
			log.error("Error al cargar propiedades: " + PROPERTY_FILE_PATH, e);
			return false;
		}
		return true;
	}
}
