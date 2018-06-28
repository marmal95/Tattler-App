package tattler.pro.tattler.authentication;

public interface FingerprintSensorCallback {
    void onAuthenticationSucceeded();
    void onAuthenticationFailed();
    void onAuthenticationError(String errorMessage);
    void onAuthenticationHelp(String helpMessage);
}
