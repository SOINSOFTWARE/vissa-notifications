package com.soinsoftware.vissa.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import com.soinsoftware.vissa.commons.CommonsConstants;

/**
 * @author alejandro@vaadin.com
 **/
public class CommonsEmailService {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		try {
			new CommonsEmailService().send("FLORLI319@GMAIL.COM", Arrays.asList(new String("florli319@gmail.com")),
					"mail prueba", "prueba");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sends an email message with no attachments.
	 *
	 * @param from       email address from which the message will be sent.
	 * @param recipients the recipients of the message.
	 * @param subject    subject header field.
	 * @param text       content of the message.
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void send(String from, Collection<String> recipients, String subject, String text)
			throws IOException, EmailException {
		send(from, recipients, subject, text, null, null, null);
	}

	/**
	 * Sends an email message to one recipient with one attachment.
	 *
	 * @param from       email address from which the message will be sent.
	 * @param recipient  the recipients of the message.
	 * @param subject    subject header field.
	 * @param text       content of the message.
	 * @param attachment attachment to be included with the message.
	 * @param fileName   file name of the attachment.
	 * @param mimeType   mime type of the attachment.
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void send(String from, String recipient, String subject, String text, InputStream attachment,
			String fileName, String mimeType) throws IOException, EmailException {
		send(from, Arrays.asList(recipient), subject, text, Arrays.asList(attachment), Arrays.asList(fileName),
				Arrays.asList(mimeType));
	}

	/**
	 * Sends an email message with attachments.
	 *
	 * @param from        email address from which the message will be sent.
	 * @param recipients  array of strings containing the recipients of the message.
	 * @param subject     subject header field.
	 * @param text        content of the message.
	 * @param attachments attachments to be included with the message.
	 * @param fileNames   file names for each attachment.
	 * @param mimeTypes   mime types for each attachment.
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void send(String from, Collection<String> recipients, String subject, String text,
			List<InputStream> attachments, List<String> fileNames, List<String> mimeTypes)
			throws EmailException, IOException {

		// check for null references
		Objects.requireNonNull(from);
		Objects.requireNonNull(recipients);

		// create an email message with html support
		HtmlEmail email = new HtmlEmail();

		// configure SMTP connection
		email.setHostName(CommonsConstants.MAIL_HOST);
		email.setSmtpPort(Integer.parseInt(CommonsConstants.MAIL_PORT));
		email.setSSL(true);
		email.setAuthentication(CommonsConstants.MAIL_USERNAME, CommonsConstants.MAIL_PASSWORD);
		// email.setSSLOnConnect(Boolean.parseBoolean(ssl));

		// set its properties accordingly
		email.setFrom(from);
		email.addTo("florli319@gmail.com");
		email.addTo("carlosandres.rodriguezpatino@gmail.com");
		email.setSubject(subject);
		email.setHtmlMsg(text);

		if (attachments != null) {
			for (int i = 0; i < attachments.size(); i++) {
				// create a data source to wrap the attachment and its mime type
				ByteArrayDataSource dataSource = new ByteArrayDataSource(attachments.get(i), mimeTypes.get(i));

				// add the attachment
				email.attach(dataSource, fileNames.get(i), "attachment");
			}
		}

		// send it!
		 email.send();

		Properties props = new Properties();

		// Nombre del host de correo, es smtp.gmail.com
		props.setProperty("mail.smtp.host", "smtp.gmail.com");

		// TLS si está disponible
		props.setProperty("mail.smtp.starttls.enable", "true");

		// Puerto de gmail para envio de correos
		props.setProperty("mail.smtp.port", "587");

		// Nombre del usuario
		props.setProperty("mail.smtp.user", "florli319@gmail.com");

		// Si requiere o no usuario y password para conectarse.
		props.setProperty("mail.smtp.auth", "true");

		Session session = Session.getInstance(props);
		session.setDebug(true);

		MimeMessage message = new MimeMessage(session);

		try {
			// Quien envia el correo
			message.setFrom(new InternetAddress("florli319@gmail.com"));
			// A quien va dirigido
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("florli319@gmail.com"));

			message.setSubject("Hola");
			message.setText("Mensajito con Java Mail" + "de los buenos." + "poque si");

			Transport t = session.getTransport("smtp");

			t.connect("florli319@gmail.com", "Google#319");
			//t.sendMessage(message, message.getAllRecipients());
			t.close();

		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
