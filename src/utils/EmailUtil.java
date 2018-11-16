package utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import com.didisoft.pgp.PGPException;
import com.didisoft.pgp.mail.PGPMailLib;
import com.didisoft.pgp.mail.PGPMailUtils;

import lw.bouncycastle.openpgp.PGPKeyPair;
import lw.bouncycastle.openpgp.PGPPublicKey;

public class EmailUtil {
	public static ArrayList<ArrayList<String>> getListDetailMessage(Message[] messages) throws MessagingException {
		ArrayList<ArrayList<String>> arrArr = new ArrayList<>();
		SimpleDateFormat smf = new SimpleDateFormat("dd-MM-yy hh:mm:ss:SSSS");

		for (int i = 0; i < messages.length; i++) {
			ArrayList<String> arr = new ArrayList<>();
			Message message = messages[i];
//			arr.add(message.getMessageNumber());
			arr.add(String.valueOf(i + 1));
			arr.add(smf.format(message.getSentDate()));
			arr.add(smf.format(message.getReceivedDate()));
			arr.add(new String(StandardCharsets.UTF_8.encode(message.getFrom()[0].toString()).array(), UTF_8));
			arr.add(message.getSubject());

			PGPMailUtils mailUtil = new PGPMailUtils();
			if (mailUtil.isOpenPGPEncrypted(message) || mailUtil.isOpenPGPSigned(message)) {
				arr.add("PGP");
			} else {
				arr.add("Normal");
			}
			arrArr.add(arr);
		}
		return arrArr;
	}

	public static String getContent(Message message, String privateKeyFile, String privateKeyPass, String publicKeyFile)
			throws Exception {
		StringBuffer buf = new StringBuffer();
		PGPMailUtils mailUtil = new PGPMailUtils();

		MimeMessage mimeMessage = new MimeMessage((MimeMessage) message);
		if (!publicKeyFile.equals("")) {
			if (mailUtil.isOpenPGPEncrypted(message)) {
				MimeMessage decrypted = mailUtil.decryptMessage(// emailSession,
						mimeMessage, privateKeyFile, privateKeyPass);

				Object decryptedRawContent = decrypted.getContent();
				if (decryptedRawContent instanceof Multipart) {
					Multipart multipart = (Multipart) decryptedRawContent;
					for (int j = 0; j < multipart.getCount(); j++) {
						BodyPart bodyPart = multipart.getBodyPart(j);

						String disposition = bodyPart.getDisposition();
						if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) { // mailUtil.hasAttachments(decrypted))
							System.out.println("Mail have some attachment");
							buf.append("Mail have some attachment.\n");
							DataHandler handler = bodyPart.getDataHandler();
							System.out.println("file name : " + handler.getName());
							buf.append("file name : " + handler.getName() + "\n");
						} else {
							String content = getText(bodyPart);
							System.out.println(content);
							if (content != null) {
								buf.append(content + "\n");
							}
						}
					}
				} else {
					String content = decryptedRawContent.toString();
					buf.append(content + "\n");
				}

				String r = "";
				
				if (mailUtil.verify(decrypted, publicKeyFile)) {
					r = "[verified]";
				} else {
					r = "[no sign/no verified]";
				}
				
				buf.insert(0, "Body " + r + "\n");
				return buf.toString();
			}

			if (mailUtil.isOpenPGPSigned((Message) mimeMessage)) {
				MimeBodyPart decrypted = mailUtil.getSignedContent((MimeMessage) message);
				String content = getText(decrypted);
				buf.append(content + "\n");
				String r;
				if (mailUtil.verify((MimeMessage) message, publicKeyFile)) {
					r = "[verified]";
				} else {
					r = "[not verified]";
				}
				buf.insert(0, "Body " + r + "\n");
				return buf.toString();
			}
		} else {
			throw new Exception("Public key not found");
		}
		if (mimeMessage.getContent() instanceof Multipart) {
			Multipart multipart = (Multipart) mimeMessage.getContent();
			for (int j = 0; j < multipart.getCount(); j++) {
				BodyPart bodyPart = multipart.getBodyPart(j);

				String disposition = bodyPart.getDisposition();
				if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) { // mailUtil.hasAttachments(decrypted))
					System.out.println("Mail have some attachment");
					buf.append("Mail have some attachment.\n");
					DataHandler handler = bodyPart.getDataHandler();
					System.out.println("file name : " + handler.getName());
					buf.append("file name : " + handler.getName() + "\n");
				} else {
					String content = getText(bodyPart);
					System.out.println(content);
					buf.append(content + "\n");
				}
			}
		} else {
			String content = mimeMessage.getContent().toString();
			buf.append(content + "\n");
		}
		return buf.toString();
	}

	private static String getText(Part p) throws MessagingException, IOException {
		if (p.isMimeType("text/*")) {
			String s = (String) p.getContent();
			return s;
		}

		if (p.isMimeType("multipart/alternative")) {
			// prefer html text over plain text
			Multipart mp = (Multipart) p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null)
						text = getText(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null)
						return s;
				} else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}

		return null;
	}
}
