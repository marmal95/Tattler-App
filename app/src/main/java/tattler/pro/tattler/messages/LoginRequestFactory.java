package tattler.pro.tattler.messages;

import android.content.Context;

import java.io.Serializable;

import tattler.pro.tattler.common.AppPreferences;

public class LoginRequestFactory implements Serializable {
    private static final long serialVersionUID = 5812262185067188602L;

    public LoginRequest create(Context context) {
        AppPreferences appPreferences = AppPreferences.getInstance(context);
        String phoneNumber = appPreferences.getString(AppPreferences.Key.USER_NUMBER, "");
        String userName = appPreferences.getString(AppPreferences.Key.USER_NAME, "");
        return new LoginRequest(phoneNumber, userName);
    }
}
