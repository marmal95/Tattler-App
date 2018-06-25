package tattler.pro.tattler.security;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    private FingerprintSensorCallback callback;
    private CancellationSignal cancellationSignal;

    FingerprintHandler(Context context, FingerprintSensorCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        callback.onAuthenticationError(errString.toString());
    }

    @Override
    public void onAuthenticationFailed() {
        callback.onAuthenticationFailed();
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        callback.onAuthenticationHelp(helpString.toString());
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        callback.onAuthenticationSucceeded();
    }

    public void startAuthentication(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        cancellationSignal = new CancellationSignal();
        if (!hasFingerprintPermission()) {
            return;
        }

        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    public void stopAuthentication() {
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }

    private boolean hasFingerprintPermission() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED;
    }
}