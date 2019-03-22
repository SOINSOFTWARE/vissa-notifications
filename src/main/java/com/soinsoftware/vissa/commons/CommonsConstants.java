package com.soinsoftware.vissa.commons;

import org.apache.log4j.Logger;

public class CommonsConstants {

	protected static final Logger log = Logger.getLogger(CommonsConstants.class);

	private static final String PROPERTY_FILE_PATH = "E:\\Documents\\GitHub\\vissa-notifications\\src\\main\\resources\\vissa-notification.properties";

	public static String FORMAT_DATE_TIME;
	public static String FORMAT_DATE;
	public static String SMS_ACCOUNT_ID;
	public static String SMS_AUTH_TOKEN;
	public static String SMS_PHONES_TO;
	public static String SMS_PHONES_FROM;
	public static Integer EXPIRATION_DAYS;
	public static String MESSAGE_EXPIRATION;
	public static String MAIL_HOST;
	public static String MAIL_PORT;
	public static String MAIL_USERNAME;
	public static String MAIL_PASSWORD;
	public static String MAIL_FROM;
	public static String MAIL_TO;
	public static String MAIL_CONCILIATION_SUBJECT;
	public static String SALESMAN_CONCILIATION_MSG;
	public static String ADMON_CONCILIATION_MSG;

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

			MAIL_HOST = properties.getTextProperty("mail.smtp.host");
			MAIL_PORT = properties.getTextProperty("mail.smtp.port");
			MAIL_USERNAME = properties.getTextProperty("mail.smtp.username").trim();
			MAIL_PASSWORD = properties.getTextProperty("mail.smtp.password").trim();
			MAIL_FROM = properties.getTextProperty("mail.smtp.from").trim();
			MAIL_TO = properties.getTextProperty("mail.smtp.to").trim();
			MAIL_CONCILIATION_SUBJECT = properties.getTextProperty("mail.smtp.conciliation.subject").trim();

			SALESMAN_CONCILIATION_MSG = "<html>\r\n" + 
					"<h1> Cuadre de caja vendedor SALESMAN</h1>\r\n" + 
					"<p>\r\n" + 
					"<table border=\"1\">\r\n" + 
					"  <tr>\r\n" + 
					"    <td>Base caja</td>\r\n" + 
					"    <td>BASE</td> \r\n" + 
					"  </tr>\r\n" + 
					"  <tr>\r\n" + 
					"    <td>Caja N.2</td>\r\n" + 
					"    <td>SALES</td> \r\n" + 
					"  </tr>\r\n" + 
					"  <tr>\r\n" + 
					"    <td>Sobrante</td>\r\n" + 
					"    <td>REMANT_SALE</td> \r\n" + 
					"  </tr>\r\n" + 
					"  <tr>\r\n" + 
					"    <td>Recaudo Vtas. Crédito</td>\r\n" + 
					"    <td>CREDIT_COLLECTION</td> \r\n" + 
					"  </tr>\r\n" + 
					"    <tr>\r\n" + 
					"    <td><b>TOTAL VENTAS</b></td>\r\n" + 
					"    <td><b>TOTAL_SALE</b></td> \r\n" + 
					"  </tr>\r\n" + 
					"</table>\r\n" + 
					"</p>\r\n" + 
					"\r\n" + 
					"<p>\r\n" + 
					"<table border=\"1\">\r\n" + 
					"  <tr>\r\n" + 
					"    <td>Gastos generales</td>\r\n" + 
					"    <td>EXPENSES</td> \r\n" + 
					"  </tr>\r\n" + 
					"  <tr>\r\n" + 
					"    <td>Pago a proveedores</td>\r\n" + 
					"    <td>SUPPLIER</td> \r\n" + 
					"  </tr>\r\n" + 
					"  <tr>\r\n" + 
					"    <td>Faltante</td>\r\n" + 
					"    <td>REMANT_SALE</td> \r\n" + 
					"  </tr>\r\n" + 
					"  <tr>\r\n" + 
					"    <td><b>TOTAL EGRESOS</b></td>\r\n" + 
					"    <td><b>TOTAL_EGRESS</b></td> \r\n" + 
					"  </tr>  \r\n" + 
					"</table>\r\n" + 
					"</p>\r\n" + 
					"<p>\r\n" + 
					"<table border=\"1\">\r\n" + 
					"  <tr>\r\n" + 
					"    <td><b>VENTA CREDITO</b></td>\r\n" + 
					"    <td><b>TOTAL_CREDIT</b></td> \r\n" + 
					"  </tr>\r\n" + 
					"  <tr>\r\n" + 
					"    <td><b>EFECTIVO NETO</b></td>\r\n" + 
					"    <td><b>TOTAL_CASH</b></td> \r\n" + 
					"  </tr> \r\n" + 
					"</table>\r\n" + 
					"</p>\r\n" + 
					"\r\n" + 
					"</hmtl>";
			
			
			ADMON_CONCILIATION_MSG = "<html>\r\n" + 
					"<h1> Cuadre de caja administrador ADMINISTRATOR</h1>\r\n" + 
					"<p>\r\n" + 
					"<table border=\"1\">\r\n" + 
					"  <tr>\r\n" + 
					"    <td>Base caja</td>\r\n" + 
					"    <td>BASE</td> \r\n" + 
					"  </tr>\r\n" + 
					"  <tr>\r\n" + 
					"    <td>Pago a proveedores</td>\r\n" + 
					"    <td align=\"right\">SUPPLIER_PAYMENTS</td> \r\n" + 
					"  </tr>\r\n" + 
					"  <tr>\r\n" + 
					"    <td>Préstamo para pago a proveedores</td>\r\n" + 
					"    <td>SUPPLIER_LOANS</td> \r\n" + 
					"  </tr>\r\n" + 
					"  <tr>\r\n" + 
					"    <td><b>Saldo</b></td>\r\n" + 
					"    <td>BALANCE</td> \r\n" + 					
					"</table>\r\n" + 
					"</p>\r\n" + 
					"\r\n" + 
					"<p>\r\n" + 
			
					"</hmtl>";

		} catch (Exception e) {
			log.error("Error al cargar propiedades: " + PROPERTY_FILE_PATH, e);
			return false;
		}
		return true;
	}
}
