package tattler.pro.tattler.messages;

import android.content.Context;
import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class MessageFactory {
    private Context context;

    public MessageFactory(Context context) {
        this.context = context;
    }

    public LoginRequest createLoginRequest() {
        AppPreferences appPreferences = AppPreferences.getInstance(context);
        String phoneNumber = appPreferences.getString(AppPreferences.Key.USER_PHONE_NUMBER, "");
        String userName = appPreferences.getString(AppPreferences.Key.USER_NAME, "");
        boolean areContactsRequested = appPreferences.getBoolean(AppPreferences.Key.IS_FIRST_LAUNCH, true);
        return new LoginRequest(phoneNumber, userName, areContactsRequested);
    }

    public CreateChatRequest createCreateChatRequest(Contact contact) {
        AppPreferences appPreferences = AppPreferences.getInstance(context);
        int userNumber = appPreferences.getInt(AppPreferences.Key.USER_NUMBER);

        List<Integer> chatContacts = new ArrayList<>();
        chatContacts.add(contact.contactNumber);

        return new CreateChatRequest(userNumber, chatContacts, contact.contactName, false);
    }
}
