package tattler.pro.tattler.security;

public interface FingerprintSensorCallback {
    void onAuthenticationSucceeded();
    void onAuthenticationFailed();
    void onAuthenticationError(String errorMessage);
    void onAuthenticationHelp(String helpMessage);
}
