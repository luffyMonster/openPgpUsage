package utils;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.didisoft.pgp.CompressionAlgorithm;
import com.didisoft.pgp.CypherAlgorithm;
import com.didisoft.pgp.HashAlgorithm;
import com.didisoft.pgp.KeyAlgorithm;
import com.didisoft.pgp.PGPException;
import com.didisoft.pgp.PGPKeyPair;

import app.App;

public class KeyGenerate {
	public static void make(String mailAddress, String publicPath, String privatePath, int keySizeInBytes) throws PGPException, IOException {

		String keyAlgorithm = KeyAlgorithm.RSA;

		String userId = mailAddress;
		String[] hashingAlgorithms = new String[] { HashAlgorithm.SHA1, HashAlgorithm.SHA256, HashAlgorithm.SHA384,
				HashAlgorithm.SHA512, HashAlgorithm.MD5 };

		String[] compressions = new String[] { CompressionAlgorithm.ZIP, CompressionAlgorithm.ZLIB,
				CompressionAlgorithm.UNCOMPRESSED };

		String[] cyphers = new String[] { CypherAlgorithm.CAST5, CypherAlgorithm.AES_128, CypherAlgorithm.AES_192,
				CypherAlgorithm.AES_256, CypherAlgorithm.TWOFISH };

		String privateKeyPassword = "abcdef";

		long expiresAfterDays = 0;

		PGPKeyPair keypair = PGPKeyPair.generateKeyPair(keySizeInBytes, userId, keyAlgorithm, privateKeyPassword,
				compressions, hashingAlgorithms, cyphers, expiresAfterDays);
		File file = new File(privatePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		File file1 = new File(publicPath);
		if (!file1.exists()) {
			file1.mkdirs();
		}
		keypair.exportPrivateKey(privatePath + "privatekey.skr", true);
		keypair.exportPublicKey(publicPath + "publickey.pkr", true);
//		keypair.exportKeyRing(path + "pgp.keystore", true);

	}
}
