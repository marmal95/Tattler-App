package tattler.pro.tattler.messages;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Contact;
import tattler.pro.tattler.models.Invitation;
import tattler.pro.tattler.security.RsaCrypto;

public class MessageFactory {
    private Context context;

    public MessageFactory(Context context) {
        this.context = context;
    }

    public LoginRequest createLoginRequest() {
        AppPreferences appPreferences = AppPreferences.getInstance(context);
        String phoneNumber = appPreferences.getString(AppPreferences.Key.USER_PHONE_NUMBER);
        String userName = appPreferences.getString(AppPreferences.Key.USER_NAME);
        boolean areContactsRequested = appPreferences.getBoolean(AppPreferences.Key.IS_FIRST_LAUNCH, true);
        return new LoginRequest(phoneNumber, userName, areContactsRequested);
    }

    public CreateChatRequest createCreateChatRequest(Contact contact) {
        List<Integer> chatContacts = new ArrayList<>();
        chatContacts.add(contact.contactNumber);
        return new CreateChatRequest(getMyUserNumber(), chatContacts, null, false);
    }

    public ChatInvitation createChatInvitation(CreateChatResponse chatResponse) {
        ChatInvitation chatInvitation = new ChatInvitation(
                getMyUserNumber(),
                chatResponse.chatId,
                chatResponse.isGroupChat,
                chatResponse.chatName);
        chatInvitation.chatContacts.addAll(chatResponse.contacts);
        chatInvitation.chatContacts.add(new tattler.pro.tattler.messages.models.Contact(getMyUserNumber(), getMyUserName()));
        return chatInvitation;
    }

    public AddContactRequest createAddContactRequest(int contactPhoneId) {
        int userPhoneId = getMyUserNumber();
        return new AddContactRequest(contactPhoneId, userPhoneId);
    }

    public ChatInvitationResponse createChatInvitationResponse(Invitation invitation, int status, byte[] publicKey) {
        ChatInvitationResponse invitationResponse = new ChatInvitationResponse(
                getMyUserNumber(),
                invitation.senderId,
                invitation.invitationMessageId,
                status);

        invitationResponse.publicKey = publicKey;
        invitationResponse.chatId = invitation.chat.chatId;
        return invitationResponse;
    }

    public InitializeChatIndication createInitializeChatIndication(
            ChatInvitationResponse chatInvitationResponse, Chat chat, RsaCrypto rsaCrypto) throws Exception {
        InitializeChatIndication initChatInd = new InitializeChatIndication(
                getMyUserNumber(),
                chatInvitationResponse.senderId);
        initChatInd.chatId = chatInvitationResponse.chatId;
        initChatInd.chatEncryptedKey = rsaCrypto.encrypt(chat.chatKey, chatInvitationResponse.publicKey);
        return initChatInd;
    }

    private int getMyUserNumber() {
        AppPreferences appPreferences = AppPreferences.getInstance(context);
        return appPreferences.getInt(AppPreferences.Key.USER_NUMBER);
    }

    private String getMyUserName() {
        AppPreferences appPreferences = AppPreferences.getInstance(context);
        return appPreferences.getString(AppPreferences.Key.USER_NAME);
    }
}
