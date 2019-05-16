package net.eclever.stations.backend;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

class Scratch {
	public static void main(String[] args) throws Exception {
		String key = "2019-05-16";
		String text = "ios_stations_7765ff1c-a69c-417f-b224-5228e9f48f84";

		//encrypt
		SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		byte[] encryptedData = cipher.doFinal(text.getBytes());;

		//decrypt
		String ecnryptedText = (new BASE64Encoder().encode(encryptedData));
		System.out.println(ecnryptedText);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		encryptedData = cipher.doFinal(new BASE64Decoder().decodeBuffer(ecnryptedText));

		System.out.println(new String(encryptedData));
	}
}