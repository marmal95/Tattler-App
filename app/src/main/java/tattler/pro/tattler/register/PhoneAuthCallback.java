package tattler.pro.tattler.register;

public interface PhoneAuthCallback {
    void onVerificationCompleted(String phoneNumber);
    void onVerificationFailed(String failMessage);
    void onVerificationCodeSent();
}
