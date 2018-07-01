package tattler.pro.tattler.splash;

import android.content.Context;
import tattler.pro.tattler.util.AppPreferences;

public class UserLoginStatusChecker {
    private AppPreferences appPreferences;

    UserLoginStatusChecker(Context context) {
        this.appPreferences = AppPreferences.getInstance(context);
    }

    boolean isUserLoggedIn() {
        String phoneNumber = appPreferences.getString(AppPreferences.Key.USER_PHONE_NUMBER);
        return phoneNumber != null && !phoneNumber.isEmpty();
    }
}
