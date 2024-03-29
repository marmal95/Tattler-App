package tattler.pro.tattler.messages;

import java.util.ArrayList;
import java.util.List;

import tattler.pro.tattler.messages.models.Contact;

public class ChatInvitation extends Message {
    private static final long serialVersionUID = -5646373887571573954L;
    public int chatId;
    public boolean isGroupChat;
    public String chatName;
    public List<Contact> chatContacts;

    public ChatInvitation(int senderId, int receiverId, int chatId, boolean isGroupChat, String chatName) {
        super(Type.CHAT_INVITATION);
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.chatId = chatId;
        this.isGroupChat = isGroupChat;
        this.chatName = chatName;
        this.chatContacts = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ChatInvitation{" +
                "chatId=" + chatId +
                ", isGroupChat=" + isGroupChat +
                ", chatName='" + chatName + '\'' +
                ", chatContacts=" + chatContacts +
                ", messageType=" + messageType +
                ", messageTime=" + messageTime +
                ", messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}
