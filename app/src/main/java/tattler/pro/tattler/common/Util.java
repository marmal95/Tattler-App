package tattler.pro.tattler.common;

import android.content.Context;

import tattler.pro.tattler.R;

public class Util {
    public static String pickHexColor(Context applicationContext, String text) {
        String[] avatarColors = applicationContext.getResources().getStringArray(R.array.avatarsColors);
        int colorIndex = Math.abs(text.hashCode()) % avatarColors.length;
        return avatarColors[colorIndex];
    }

    public static String extractUserInitials(String userName) {
        if (userName == null || userName.isEmpty()) {
            return "-";
        }
        return String.valueOf(userName.toUpperCase().charAt(0));
    }

    public static boolean isMessageSentByMe(Context context, int messageSenderUserNumber) {
        AppPreferences appPreferences = AppPreferences.getInstance(context);
        int myNumber = appPreferences.getInt(AppPreferences.Key.USER_NUMBER);
        return myNumber == messageSenderUserNumber;
    }
}
