package tattler.pro.tattler.messages;

import android.content.Context;
import tattler.pro.tattler.util.AppPreferences;

import java.io.Serializable;

public class LoginRequestFactory implements Serializable {
    private static final long serialVersionUID = 5812262185067188602L;

    public LoginRequest create(Context context) {
        AppPreferences appPreferences = AppPreferences.getInstance(context);
        String phoneNumber = appPreferences.getString(AppPreferences.Key.USER_NUMBER, "");
        String userName = appPreferences.getString(AppPreferences.Key.USER_NAME, "");
        int phoneId = appPreferences.getInt(AppPreferences.Key.USER_NUMBER, 0);
        return new LoginRequest(phoneNumber, userName, phoneId);
    }
}
