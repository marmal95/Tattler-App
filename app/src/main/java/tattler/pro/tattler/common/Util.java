package tattler.pro.tattler.common;

import android.content.Context;

import java.util.List;
import java.util.stream.Collectors;

import tattler.pro.tattler.R;
import tattler.pro.tattler.messages.models.Contact;

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

    public static int getMyUserNumber(Context context) {
        AppPreferences appPreferences = AppPreferences.getInstance(context);
        return appPreferences.getInt(AppPreferences.Key.USER_NUMBER);
    }

    public static String generateChatName(List<Contact> contacts) {
        return contacts.stream().map(contact -> contact.contactName).collect(Collectors.joining(" - "));
    }
}
