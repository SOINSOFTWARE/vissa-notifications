package com.soinsoftware.vissa.util;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.soinsoftware.vissa.commons.CommonsConstants;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsGenerator {

	protected static final Logger log = Logger.getLogger(SmsGenerator.class);

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		new SmsGenerator().sendSMS("sendSMS");
	}

	public static void sendSMS(String smsMessage) {
		String strLog = "[sendSMS] ";
		try {
			Twilio.init(CommonsConstants.SMS_ACCOUNT_ID, CommonsConstants.SMS_AUTH_TOKEN);

			PhoneNumber phoneFrom = new PhoneNumber(CommonsConstants.SMS_PHONES_FROM);
			// Se toman los destinatarios del archivo de propiedades
			String[] phonesArr = CommonsConstants.SMS_PHONES_TO.split(",");
			List<String> phonesTo = Arrays.asList(phonesArr);

			for (String phoneObj : phonesTo) {
				PhoneNumber phoneTo = new PhoneNumber(phoneObj);
				Message message = Message.creator(phoneTo, phoneFrom, smsMessage).create();
				log.info(strLog + message.getSid());
				log.info(strLog + "Mensaje enviado:" + message);
			}
		} catch (Exception e) {
			log.error(strLog + "[Exception] " + e.getMessage());
		}
	}
}
