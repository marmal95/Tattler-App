package tattler.pro.tattler.authentication;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.orhanobut.logger.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import tattler.pro.tattler.R;
import tattler.pro.tattler.register.PhoneAuthCallback;

public class PhoneAuthenticator {
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks authCallbacks;
    private PhoneAuthProvider.ForceResendingToken authResendToken;

    private PhoneAuthCallback phoneAuthCallback;
    private Context context;
    private String verificationId;
    private boolean isAuthInProgress;

    public PhoneAuthenticator(Context context) {
        Logger.d("PhoneAuthenticator object created.");
        this.context = context;
        phoneAuthCallback = null;
        isAuthInProgress = false;

        FirebaseApp.initializeApp(context);
        firebaseAuth = FirebaseAuth.getInstance();
        authCallbacks = new PhoneAuthenticationCallback();
    }

    public void startPhoneNumberVerification(String phoneNumber, PhoneAuthCallback callback) {
        if (isAuthInProgress) {
            Logger.d("Phone authentication already running. Actual will be discarded.");
            callback.onVerificationFailed(context.getString(R.string.phoneAuthentication));
        } else {
            isAuthInProgress = true;
            phoneAuthCallback = callback;
            startPhoneNumberVerification(phoneNumber);
        }
    }

    public void verifyPhoneNumberWithCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
        Logger.d("Code verification procedure started.");
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, Executors.newCachedThreadPool(), authCallbacks);
        Logger.d("Phone verification procedure started.");
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = task.getResult().getUser();
                String userPhoneNumber = user.getPhoneNumber();
                phoneAuthCallback.onVerificationCompleted(userPhoneNumber);
            } else {
                phoneAuthCallback.onVerificationFailed(context.getString(R.string.verificationCodeIncorrect));
            }
        });
    }

    private class PhoneAuthenticationCallback extends PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Logger.d("Phone number verification completed.");
            isAuthInProgress = false;
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Logger.d("Phone number verification failed.");
            isAuthInProgress = false;
            phoneAuthCallback.onVerificationFailed(context.getString(R.string.phoneAuthenticationFail));
        }

        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Logger.d("Verification code sent.");
            super.onCodeSent(verificationId, forceResendingToken);
            PhoneAuthenticator.this.verificationId = verificationId;
            authResendToken = forceResendingToken;
            phoneAuthCallback.onVerificationCodeSent();
        }
    }
}
