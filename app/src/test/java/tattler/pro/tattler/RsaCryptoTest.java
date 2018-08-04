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
    private final byte[] INVALID_KEY = "invalid_key".getBytes();
    private RsaCrypto rsaCrypto;
    private KeyPair keyPair;

    @Before
    public void setUp() {
        rsaCrypto = new RsaCrypto();
        keyPair = rsaCrypto.generateRsaKeyPair();
    }

    @Test(expected = GeneralSecurityException.class)
    public void shallRsaCryptoInitThrowExceptionWhenKeysInvalid() throws Exception {
        new RsaCrypto().init(INVALID_KEY, INVALID_KEY);
    }

    @Test
    public void shallRsaCryptoInitNotThrowExceptionWhenKeysValid() {
        try {
            rsaCrypto.init(keyPair.getPublic().getEncoded(), keyPair.getPrivate().getEncoded());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void shallRsaCryptoInitNotThrowExceptionWhenKeyPairValid() {
        try {
            rsaCrypto.init(keyPair);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
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
        rsaCrypto.init(keyPair.getPublic().getEncoded(), keyPair.getPrivate().getEncoded());
        expectEncryptAndDecryptMessages();
    }

    @Test
    public void shouldEncryptAndDecryptWithPassedKeyPair() throws Exception {
        rsaCrypto.init(keyPair);
        expectEncryptAndDecryptMessages();
    }

    @Test
    public void shallEncryptAndDecryptDividedIntoBlocksWhenMessageIsBigWithPassedKeys() throws GeneralSecurityException {
        rsaCrypto.init(keyPair.getPublic().getEncoded(), keyPair.getPrivate().getEncoded());
        expectEncryptAndDecryptBigMessage();
    }

    @Test
    public void shallEncryptAndDecryptDividedIntoBlocksWhenMessageIsBigWithPassedKeyPair() throws GeneralSecurityException {
        rsaCrypto.init(keyPair);
        expectEncryptAndDecryptBigMessage();
    }

    @Test
    public void shallEncryptAndDecryptByteArrayWithPassedKeys() throws GeneralSecurityException {
        rsaCrypto.init(keyPair.getPublic().getEncoded(), keyPair.getPrivate().getEncoded());
        expectEncryptAndDecryptRandomBytes();
    }

    @Test
    public void shallEncryptAndDecryptByteArrayWithPassedKeyPair() throws GeneralSecurityException {
        rsaCrypto.init(keyPair);
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