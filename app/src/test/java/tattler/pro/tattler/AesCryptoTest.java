package tattler.pro.tattler;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import tattler.pro.tattler.security.AesCrypto;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;


public class AesCryptoTest {
    private AesCrypto aesCrypto;

    @Before
    public void setUp() {
        aesCrypto = new AesCrypto();
    }

    @Test
    public void shouldNotGenerateNullKey() {
        byte[] key = aesCrypto.generateAesKey();
        assertNotNull(key);
    }

    @Test
    public void shallEncryptAndDecryptRandomBytes() throws Exception {
        initAesCryptoWithRandomKey();
        expectEncryptAndDecryptRandomBytes();
    }

    @Test
    public void shouldEncryptAndDecryptMessages() throws Exception {
        initAesCryptoWithRandomKey();
        expectEncryptAndDecryptMessages();
    }

    @Test
    public void shouldEncryptAndDecryptBigMessage() throws Exception {
        initAesCryptoWithRandomKey();
        expectEncryptAndDecryptBigMessage();
    }

    private void expectEncryptAndDecryptMessages() throws Exception {
        byte[] encryptedMessage_1 = aesCrypto.encrypt(TestMessages.message_1.getBytes());
        byte[] decryptedMessage_1 = aesCrypto.decrypt(encryptedMessage_1);
        assertArrayEquals(TestMessages.message_1.getBytes(), decryptedMessage_1);

        byte[] encryptedMessage_2 = aesCrypto.encrypt(TestMessages.message_2.getBytes());
        byte[] decryptedMessage_2 = aesCrypto.decrypt(encryptedMessage_2);
        assertArrayEquals(TestMessages.message_2.getBytes(), decryptedMessage_2);

        byte[] encryptedMessage_3 = aesCrypto.encrypt(TestMessages.message_3.getBytes());
        byte[] decryptedMessage_3 = aesCrypto.decrypt(encryptedMessage_3);
        assertArrayEquals(TestMessages.message_3.getBytes(), decryptedMessage_3);
    }

    private void expectEncryptAndDecryptBigMessage() throws Exception {
        byte[] encryptedMessage_4 = aesCrypto.encrypt(TestMessages.message_4.getBytes());
        byte[] decryptedMessage_4 = aesCrypto.decrypt(encryptedMessage_4);
        assertArrayEquals(TestMessages.message_4.getBytes(), decryptedMessage_4);
    }

    private void expectEncryptAndDecryptRandomBytes() throws Exception {
        int test_array_size = 256000;
        byte[] randomBytes = new byte[test_array_size];
        new Random().nextBytes(randomBytes);

        byte[] encryptedBytes = aesCrypto.encrypt(randomBytes);
        byte[] decryptedBytes = aesCrypto.decrypt(encryptedBytes);
        assertArrayEquals(randomBytes, decryptedBytes);
    }

    private void initAesCryptoWithRandomKey() {
        byte[] key = aesCrypto.generateAesKey();
        aesCrypto.init(key);
    }
}
