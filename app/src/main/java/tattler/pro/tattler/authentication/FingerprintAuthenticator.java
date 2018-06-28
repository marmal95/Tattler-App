package tattler.pro.tattler.authentication;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import com.orhanobut.logger.Logger;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import tattler.pro.tattler.R;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;


public class FingerprintAuthenticator {
    private static final String KEY_NAME = "TATTLER_FINGERPRINT_KEY";
    private static final String SENSOR_OK = "FingerSensor_OK";

    private Cipher cipher;
    private KeyStore keyStore;

    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;
    private FingerprintHandler fingerprintHandler;

    private Context context;

    public FingerprintAuthenticator(Context context) {
        this.context = context;
        keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);
    }

    public void startAuthentication(FingerprintSensorCallback callback) {
        String sensorStatus = checkFingerSensorStatus();
        Logger.d("FingerprintAuthenticator status: " + sensorStatus);

        if (isSensorReadyToUse(sensorStatus)) {
            try {
                generateKey();
                initCipher();

                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                fingerprintHandler = new FingerprintHandler(context, callback);
                fingerprintHandler.startAuthentication(fingerprintManager, cryptoObject);
            } catch (Exception e) {
                Logger.d("FingerprintAuthenticator exception: " + e.getMessage());
                callback.onAuthenticationError(context.getString(R.string.fingerSensorError));
            }
        } else {
            callback.onAuthenticationError(sensorStatus);
        }

    }

    public void stopAuthentication() {
        if (fingerprintHandler != null) {
            fingerprintHandler.stopAuthentication();
            fingerprintHandler = null;
        }
    }

    private String checkFingerSensorStatus() {
        if (fingerprintManager != null && !fingerprintManager.isHardwareDetected()) {
            return context.getString(R.string.fingerprintNotSupported);
        }

        if (context.checkSelfPermission(Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return context.getString(R.string.fingerprintNoPermission);
        }

        if (!fingerprintManager.hasEnrolledFingerprints()) {
            return context.getString(R.string.fingerprintNotConfigured);
        }

        if (keyguardManager != null && !keyguardManager.isKeyguardSecure()) {
            return context.getString(R.string.lockScreenNotEnabled);
        }

        return SENSOR_OK;
    }

    private boolean isSensorReadyToUse(String sensorStatus) {
        return sensorStatus.equals(SENSOR_OK);
    }

    private void generateKey() throws Exception {
        keyStore = KeyStore.getInstance("AndroidKeyStore");
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        keyStore.load(null);
        keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build());
        keyGenerator.generateKey();
    }

    private void initCipher() throws Exception {
        cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" +
                KeyProperties.BLOCK_MODE_CBC + "/" +
                KeyProperties.ENCRYPTION_PADDING_PKCS7);

        keyStore.load(null);
        SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }
}
