package tattler.pro.tattler;


import org.junit.Before;
import org.junit.Test;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.Random;

import tattler.pro.tattler.security.RsaCrypto;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class RsaCryptoTest {
    private RsaCrypto rsaCrypto;
    private byte[] publicKey;
    private byte[] privateKey;

    @Before
    public void setUp() {
        rsaCrypto = new RsaCrypto();
        KeyPair keyPair = rsaCrypto.generateRsaKeyPair();
        publicKey = keyPair.getPublic().getEncoded();
        privateKey = keyPair.getPrivate().getEncoded();
    }

    @Test
    public void shouldConstructorNotThrowException() {
        try {
            new RsaCrypto();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void shouldNotGenerateNullKeyPair() {
        KeyPair keyPair = rsaCrypto.generateRsaKeyPair();
        assertNotNull(keyPair);
    }

    @Test
    public void shouldEncryptAndDecryptWithPassedKeys() throws Exception {
        expectEncryptAndDecryptMessages();
    }

    @Test
    public void shallEncryptAndDecryptDividedIntoBlocksWhenMessageIsBigWithPassedKeys() throws GeneralSecurityException {
        expectEncryptAndDecryptBigMessage();
    }

    @Test
    public void shallEncryptAndDecryptByteArrayWithPassedKeys() throws GeneralSecurityException {
        expectEncryptAndDecryptRandomBytes();
    }

    private void expectEncryptAndDecryptMessages() throws GeneralSecurityException {
        byte[] encryptedMessage_1 = rsaCrypto.encrypt(TestMessages.message_1.getBytes(), publicKey);
        byte[] decryptedMessage_1 = rsaCrypto.decrypt(encryptedMessage_1, privateKey);
        assertArrayEquals(TestMessages.message_1.getBytes(), decryptedMessage_1);

        byte[] encryptedMessage_2 = rsaCrypto.encrypt(TestMessages.message_2.getBytes(), publicKey);
        byte[] decryptedMessage_2 = rsaCrypto.decrypt(encryptedMessage_2, privateKey);
        assertArrayEquals(TestMessages.message_2.getBytes(), decryptedMessage_2);

        byte[] encryptedMessage_3 = rsaCrypto.encrypt(TestMessages.message_3.getBytes(), publicKey);
        byte[] decryptedMessage_3 = rsaCrypto.decrypt(encryptedMessage_3, privateKey);
        assertArrayEquals(TestMessages.message_3.getBytes(), decryptedMessage_3);
    }

    private void expectEncryptAndDecryptBigMessage() throws GeneralSecurityException {
        byte[] encryptedMessage_4 = rsaCrypto.encrypt(TestMessages.message_4.getBytes(), publicKey);
        byte[] decryptedMessage_4 = rsaCrypto.decrypt(encryptedMessage_4, privateKey);
        assertArrayEquals(TestMessages.message_4.getBytes(), decryptedMessage_4);
    }

    private void expectEncryptAndDecryptRandomBytes() throws GeneralSecurityException {
        int test_array_size = 256000;
        byte[] randomBytes = new byte[test_array_size];
        new Random().nextBytes(randomBytes);

        byte[] encryptedBytes = rsaCrypto.encrypt(randomBytes, publicKey);
        byte[] decryptedBytes = rsaCrypto.decrypt(encryptedBytes, privateKey);
        assertArrayEquals(randomBytes, decryptedBytes);
    }
}