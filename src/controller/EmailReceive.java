package controller;


import static java.nio.charset.StandardCharsets.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import com.didisoft.pgp.PGPException;

public class EmailReceive {
	private boolean textIsHtml = false;
	private Store store;
	private Folder folder;
	/** Handling MIME parts */


	private Properties getServerProperties(String protocol, String host, String port) {
		Properties properties = new Properties();

		properties.put(String.format("mail.%s.host", protocol), host);
		properties.put(String.format("mail.%s.port", protocol), port);

		properties.setProperty(String.format("mail.%s.socketFactory.class", protocol),
				"javax.net.ssl.SSLSocketFactory");
		properties.setProperty(String.format("mail.%s.socketFactory.fallback", protocol), "false");
		properties.setProperty(String.format("mail.%s.socketFactory.port", protocol), String.valueOf(port));

		return properties;
	}

	public Message[] receiveEmail(String user, String password) throws PGPException, MessagingException, IOException {

		try {
			String protocol = "imap";
			String host = "imap.gmail.com";
			String port = "993";
			Properties properties = getServerProperties(protocol, host, port);
			Session emailSession = Session.getDefaultInstance(properties);

			store = emailSession.getStore(protocol);
			store.connect(user, password);
			
			folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			Message[] messages = folder.getMessages();

			return messages;

		} catch (NoSuchProviderException e) {
			throw e;
		} catch (MessagingException e) {
			throw e;
		}
	}
	
	protected void close() throws Throwable {
		if (folder != null)
			folder.close(false);
		if (store != null)
			store.close();
	}
}
