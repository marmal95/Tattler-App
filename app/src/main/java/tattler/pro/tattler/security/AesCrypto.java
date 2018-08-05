package tattler.pro.tattler.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCrypto {
    private static final int KEY_LENGTH = 128;
    private static final int IV_LENGTH = 16;
    private byte[] key;

    public void init(byte[] key) {
        this.key = key;
    }

    public byte[] generateAesKey() {
        byte[] key = null;
        try {
            Security.setProperty("crypto.policy", "unlimited");
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(KEY_LENGTH);
            key = keyGenerator.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert key != null;
        return key;
    }

    public byte[] encrypt(byte[] plainText) throws Exception {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(plainText);
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        encrypt(byteInputStream, byteOutputStream);
        return byteOutputStream.toByteArray();
    }

    public byte[] decrypt(byte[] cipherText) throws Exception {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(cipherText);
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        decrypt(byteInputStream, byteOutputStream);
        return byteOutputStream.toByteArray();
    }

    private void encrypt(InputStream inputStream, OutputStream outputStream) throws Exception {
        byte[] iv = generateRandomIv(outputStream);
        Cipher cipher = initializeCipher(iv, Cipher.ENCRYPT_MODE);
        encryptStream(inputStream, outputStream, cipher);
    }

    private byte[] generateRandomIv(OutputStream outputStream) throws IOException {
        SecureRandom r = new SecureRandom();
        byte[] iv = new byte[IV_LENGTH];
        r.nextBytes(iv);
        outputStream.write(iv);
        outputStream.flush();
        return iv;
    }

    private Cipher initializeCipher(byte[] iv, int encryptMode) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(encryptMode, secretKey, ivSpec);
        return cipher;
    }

    private void encryptStream(InputStream inputStream, OutputStream outputStream, Cipher cipher) throws IOException {
        outputStream = new CipherOutputStream(outputStream, cipher);
        byte[] buf = new byte[1_024];
        int numRead;
        while ((numRead = inputStream.read(buf)) >= 0) {
            outputStream.write(buf, 0, numRead);
        }
        outputStream.close();
    }

    private void decrypt(InputStream inputStream, OutputStream outputStream) throws Exception {
        byte[] iv = readIv(inputStream);
        Cipher cipher = initializeCipher(iv, Cipher.DECRYPT_MODE);
        decryptStream(inputStream, outputStream, cipher);
    }

    private byte[] readIv(InputStream inputStream) throws IOException {
        byte[] iv = new byte[IV_LENGTH];
        inputStream.read(iv);
        return iv;
    }

    private void decryptStream(InputStream inputStream, OutputStream outputStream, Cipher cipher) throws IOException {
        inputStream = new CipherInputStream(inputStream, cipher);
        byte[] buf = new byte[1024];
        int numRead;
        while ((numRead = inputStream.read(buf)) >= 0) {
            outputStream.write(buf, 0, numRead);
        }
        outputStream.close();
    }
}
