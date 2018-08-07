package tattler.pro.tattler.splash;

import android.content.Context;

import tattler.pro.tattler.common.AppPreferences;

public class UserLoginStatusChecker {
    private AppPreferences appPreferences;

    UserLoginStatusChecker(Context context) {
        this.appPreferences = AppPreferences.getInstance(context);
    }

    boolean isUserLoggedIn() {
        String phoneNumber = appPreferences.getString(AppPreferences.Key.USER_PHONE_NUMBER);
        String userName = appPreferences.getString(AppPreferences.Key.USER_NAME);
        return isUserDataSufficient(phoneNumber, userName);
    }

    private boolean isUserDataSufficient(String phoneNumber, String userName) {
        return phoneNumber != null && !phoneNumber.isEmpty() &&
                userName != null && !userName.isEmpty();
    }
}
