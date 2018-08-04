package tattler.pro.tattler.messages;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.models.Contact;
import tattler.pro.tattler.models.Invitation;

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

        return new CreateChatRequest(userNumber, chatContacts, null, false);
    }

    public ChatInvitation createChatInvitation(CreateChatResponse chatResponse) {
        AppPreferences appPreferences = AppPreferences.getInstance(context);
        int userNumber = appPreferences.getInt(AppPreferences.Key.USER_NUMBER);
        String userName = appPreferences.getString(AppPreferences.Key.USER_NAME);

        ChatInvitation chatInvitation = new ChatInvitation(
                userNumber,
                chatResponse.chatId,
                chatResponse.isGroupChat,
                chatResponse.chatName);

        chatInvitation.chatContacts.addAll(chatResponse.contacts);
        chatInvitation.chatContacts.add(new tattler.pro.tattler.messages.models.Contact(userNumber, userName));

        return chatInvitation;
    }

    public AddContactRequest createAddContactRequest(int contactPhoneId) {
        AppPreferences appPreferences = AppPreferences.getInstance(context);
        int userPhoneId = appPreferences.getInt(AppPreferences.Key.USER_NUMBER);
        return new AddContactRequest(contactPhoneId, userPhoneId);
    }

    public ChatInvitationResponse createChatInvitationResponse(Invitation invitation, int status, byte[] publicKey) {
        AppPreferences appPreferences = AppPreferences.getInstance(context);
        int userPhoneId = appPreferences.getInt(AppPreferences.Key.USER_NUMBER);

        ChatInvitationResponse invitationResponse = new ChatInvitationResponse(
                userPhoneId,
                invitation.senderId,
                invitation.invitationMessageId,
                status);

        invitationResponse.publicKey = publicKey;
        invitationResponse.chatId = invitation.chat.chatId;
        return invitationResponse;
    }
}
