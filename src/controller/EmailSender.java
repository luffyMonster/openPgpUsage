package controller;

import java.io.File;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.didisoft.pgp.mail.PGPMailUtils;

import app.App;

public class EmailSender {
	public static final int NO_ENCRYPT_AND_SIGN = 0;
	public static int ONLY_ENCRYPT_MODE = 1;
	public static int ONLY_SIGN_MODE = 2;
	public static int SIGN_AND_ENCRYPT_MODE = 3;
	
	private String sender;
	private char[] password;
	
	public EmailSender(String sender, char[] password) {
		super();
		this.sender = sender;
		this.password = password;
	}

	/**
	 * 
	 * @param receiver
	 * @param subject
	 * @param content
	 * @param pgpMode
	 * @param privateKeyFile
	 * @param keyFolder
	 * @throws Exception
	 */
	public void send(String receiver, String subject, String content, int pgpMode, String privateKeyFile, String keyFolder) throws Exception {

		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.port", "465");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");

		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sender, new String(password));
			}

		});

		try {

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(sender));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
			message.setSubject(subject);
			message.setText(content);
			PGPMailUtils mailUtil = new PGPMailUtils();
			MimeMessage pgpMessage = null;
			
			if (ONLY_ENCRYPT_MODE == pgpMode) {
				pgpMessage = mailUtil.encryptMessage(session, message, findPublicKey(keyFolder, receiver));
			} else if (ONLY_SIGN_MODE == pgpMode) {
				pgpMessage = mailUtil.signMessage(session, message, privateKeyFile, "abcdef");
			} else if (SIGN_AND_ENCRYPT_MODE == pgpMode) {
				pgpMessage = mailUtil.signMessage(session, message, privateKeyFile, "abcdef");
				pgpMessage = mailUtil.encryptMessage(session, pgpMessage, findPublicKey(keyFolder, receiver));
//				pgpMessage = mailUtil.signAndEncryptMessage(session, message, privateKeyFile, "abcdef", findPublicKey(keyFolder, receiver));
			} else {
				throw new Exception("PGP mode incorrect!");
			}
			
			Transport.send(pgpMessage);
			System.out.println("Sent message successfully....");
		} catch (Exception ex) {
			throw ex;
		}

	}
	
	public String findPublicKey(String keyFolder, String userId) {
		String path = keyFolder + "/" + userId + "/publickey.pkr";
		File file = new File(path);
		if (file.exists()) {
			return file.getAbsolutePath();
		}
		return "";
	}
}
