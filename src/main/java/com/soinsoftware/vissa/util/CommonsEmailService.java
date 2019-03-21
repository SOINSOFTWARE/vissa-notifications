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
			String text = "<b><h1>Cuadre de caja vendedor</h1></b></p>";
			text = text + "Base caja :  </n>";
			text = "<html>\r\n" + 
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
					"    <td>SALES</td> \r\n" + 
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
					"    <td><b>VENTA CRÉDITO</b></td>\r\n" + 
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
			new CommonsEmailService().send("FLORLI319@GMAIL.COM", Arrays.asList(new String("florli319@gmail.com")),
					"mail prueba", text);
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
		// email.addTo("carlosandres.rodriguezpatino@gmail.com");
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
			// t.sendMessage(message, message.getAllRecipients());
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
