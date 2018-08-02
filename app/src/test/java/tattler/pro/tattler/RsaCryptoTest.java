package tattler.pro.tattler;


import org.junit.Test;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.Random;

import tattler.pro.tattler.security.RsaCrypto;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class RsaCryptoTest {
    private final byte[] INVALID_KEY = "invalid_key".getBytes();
    private final KeyPair randomKeyPair = RsaCrypto.generateRsaKeyPair();
    private RsaCrypto rsaCrypto;

    @Test(expected = GeneralSecurityException.class)
    public void shallRsaCryptoConstructorThrowExceptionWhenKeysInvalid() throws Exception {
        new RsaCrypto(INVALID_KEY, INVALID_KEY);
    }

    @Test
    public void shallRsaCryptoConstructorNotThrowExceptionWhenKeysValid() {
        try {
            KeyPair keyPair = RsaCrypto.generateRsaKeyPair();
            new RsaCrypto(keyPair.getPublic().getEncoded(), keyPair.getPrivate().getEncoded());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shallRsaCryptoConstructorNotThrowExceptionWhenKeyPairValid() {
        try {
            KeyPair keyPair = RsaCrypto.generateRsaKeyPair();
            new RsaCrypto(keyPair);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shouldNotGenerateNullKeyPair() {
        KeyPair keyPair = RsaCrypto.generateRsaKeyPair();
        assertNotNull(keyPair);
    }

    @Test
    public void shouldEncryptAndDecryptWithPassedKeys() throws Exception {
        rsaCrypto = new RsaCrypto(randomKeyPair.getPublic().getEncoded(), randomKeyPair.getPrivate().getEncoded());
        expectEncryptAndDecryptMessages();
    }

    @Test
    public void shouldEncryptAndDecryptWithPassedKeyPair() throws Exception {
        rsaCrypto = new RsaCrypto(randomKeyPair);
        expectEncryptAndDecryptMessages();
    }

    @Test
    public void shallEncryptAndDecryptDividedIntoBlocksWhenMessageIsBigWithPassedKeys() throws GeneralSecurityException {
        rsaCrypto = new RsaCrypto(randomKeyPair.getPublic().getEncoded(), randomKeyPair.getPrivate().getEncoded());
        expectEncryptAndDecryptBigMessage();
    }

    @Test
    public void shallEncryptAndDecryptDividedIntoBlocksWhenMessageIsBigWithPassedKeyPair() throws GeneralSecurityException {
        rsaCrypto = new RsaCrypto(randomKeyPair);
        expectEncryptAndDecryptBigMessage();
    }

    @Test
    public void shallEncryptAndDecryptByteArrayWithPassedKeys() throws GeneralSecurityException {
        rsaCrypto = new RsaCrypto(randomKeyPair.getPublic().getEncoded(), randomKeyPair.getPrivate().getEncoded());
        expectEncryptAndDecryptRandomBytes();
    }

    @Test
    public void shallEncryptAndDecryptByteArrayWithPassedKeyPair() throws GeneralSecurityException {
        rsaCrypto = new RsaCrypto(randomKeyPair);
        expectEncryptAndDecryptRandomBytes();
    }

    private void expectEncryptAndDecryptMessages() throws GeneralSecurityException {
        byte[] encryptedMessage_1 = rsaCrypto.encrypt(TestMessages.message_1.getBytes());
        byte[] decryptedMessage_1 = rsaCrypto.decrypt(encryptedMessage_1);
        assertArrayEquals(TestMessages.message_1.getBytes(), decryptedMessage_1);

        byte[] encryptedMessage_2 = rsaCrypto.encrypt(TestMessages.message_2.getBytes());
        byte[] decryptedMessage_2 = rsaCrypto.decrypt(encryptedMessage_2);
        assertArrayEquals(TestMessages.message_2.getBytes(), decryptedMessage_2);

        byte[] encryptedMessage_3 = rsaCrypto.encrypt(TestMessages.message_3.getBytes());
        byte[] decryptedMessage_3 = rsaCrypto.decrypt(encryptedMessage_3);
        assertArrayEquals(TestMessages.message_3.getBytes(), decryptedMessage_3);
    }

    private void expectEncryptAndDecryptBigMessage() throws GeneralSecurityException {
        byte[] encryptedMessage_4 = rsaCrypto.encrypt(TestMessages.message_4.getBytes());
        byte[] decryptedMessage_4 = rsaCrypto.decrypt(encryptedMessage_4);
        assertArrayEquals(TestMessages.message_4.getBytes(), decryptedMessage_4);
    }

    private void expectEncryptAndDecryptRandomBytes() throws GeneralSecurityException {
        int test_array_size = 256000;
        byte[] randomBytes = new byte[test_array_size];
        new Random().nextBytes(randomBytes);

        byte[] encryptedBytes = rsaCrypto.encrypt(randomBytes);
        byte[] decryptedBytes = rsaCrypto.decrypt(encryptedBytes);
        assertArrayEquals(randomBytes, decryptedBytes);
    }
}