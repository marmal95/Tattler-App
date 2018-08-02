package tattler.pro.tattler.security;


import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.Cipher;


public class RsaCrypto {
    private static final int KEY_SIZE = 1024;
    private static final int BLOCK_SIZE = 32;

    private KeyPair keyPair;
    private Cipher cipher;

    public RsaCrypto(KeyPair rsaKeyPair) throws GeneralSecurityException {
        this(rsaKeyPair.getPublic().getEncoded(), rsaKeyPair.getPrivate().getEncoded());
    }

    public RsaCrypto(byte[] publicKey, byte[] privateKey) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        X509EncodedKeySpec encodedKeySpecPublic = new X509EncodedKeySpec(publicKey);
        PKCS8EncodedKeySpec encodedKeySpecPrivate = new PKCS8EncodedKeySpec(privateKey);

        PublicKey genPublicKey = keyFactory.generatePublic(encodedKeySpecPublic);
        PrivateKey genPrivateKey = keyFactory.generatePrivate(encodedKeySpecPrivate);

        keyPair = new KeyPair(genPublicKey, genPrivateKey);
        cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
    }

    public static KeyPair generateRsaKeyPair() {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(KEY_SIZE);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Should never happen!
        }
        assert keyPair != null;
        return keyPair;
    }

    public byte[] encrypt(byte[] message) throws GeneralSecurityException {
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        return encryptMessageDividedIntoBlocks(message);
    }

    public byte[] decrypt(byte[] encryptedMessage) throws GeneralSecurityException {
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        return trimByteArray(decryptMessageDividedIntoBlocks(encryptedMessage));
    }

    private byte[] encryptMessageDividedIntoBlocks(byte[] message) throws GeneralSecurityException {
        byte[][] messageBlocks = divideArray(message, BLOCK_SIZE);
        ArrayList<Byte> encryptedMessage = new ArrayList<>();

        for (byte[] msgBlock : messageBlocks) {
            byte[] encryptedBlock = encryptBlock(msgBlock);
            appendBytesToArrayList(encryptedMessage, encryptedBlock);
        }

        return getRawBytesArray(encryptedMessage);
    }

    private byte[] decryptMessageDividedIntoBlocks(byte[] message) throws GeneralSecurityException {
        byte[][] messageBlocks = divideArray(message, KEY_SIZE / 8);
        ArrayList<Byte> decryptedMessage = new ArrayList<>();

        for (byte[] msgBlock : messageBlocks) {
            byte[] decryptedBlock = decryptBlock(msgBlock);
            appendBytesToArrayList(decryptedMessage, decryptedBlock);
        }

        return getRawBytesArray(decryptedMessage);
    }

    private static byte[][] divideArray(byte[] array, int blockSize) {
        byte[][] arrayBlocks = new byte[(int) Math.ceil(array.length / (double) blockSize)][blockSize];

        int start = 0;
        for (int i = 0; i < arrayBlocks.length; i++) {
            arrayBlocks[i] = Arrays.copyOfRange(array, start, start + blockSize);
            start += blockSize;
        }

        return arrayBlocks;
    }

    private static void appendBytesToArrayList(ArrayList<Byte> arrayList, byte[] bytes) {
        for (byte b : bytes)
            arrayList.add(b);
    }

    private static byte[] getRawBytesArray(ArrayList<Byte> byteArrayList) {
        byte[] bytes = new byte[byteArrayList.size()];
        for (int i = 0; i < byteArrayList.size(); ++i) {
            bytes[i] = byteArrayList.get(i);
        }
        return bytes;
    }

    private static byte[] trimByteArray(byte[] bytes) {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) {
            --i;
        }
        return Arrays.copyOf(bytes, i + 1);
    }

    private byte[] encryptBlock(byte[] block) throws GeneralSecurityException {
        return cipher.doFinal(block);
    }

    private byte[] decryptBlock(byte[] block) throws GeneralSecurityException {
        return cipher.doFinal(block);
    }
}