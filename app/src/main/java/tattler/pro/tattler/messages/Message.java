package tattler.pro.tattler.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private static final long serialVersionUID = 9213114637524335211L;
    public int messageType;
    public long messageTime;
    public long messageId;
    public int senderId;
    public int receiverId;

    public Message(int messageType) {
        this.messageType = messageType;
        this.messageTime = System.currentTimeMillis();
        this.messageId = this.messageTime; // MessageId is UNIX time value
        this.senderId = 0;
        this.receiverId = 0;
    }

    public class Type {
        public static final int NULL_MESSAGE = 0;
        public static final int LOGIN_REQUEST = 1;
        public static final int LOGIN_RESPONSE = 2;
        public static final int ADD_CONTACT_REQUEST = 3;
        public static final int ADD_CONTACT_RESPONSE = 4;
        public static final int CREATE_CHAT_REQUEST = 5;
        public static final int CREATE_CHAT_RESPONSE = 6;
        public static final int CHAT_INVITATION = 7;
        public static final int CHAT_INVITATION_RESPONSE = 8;
        public static final int INITIALIZE_CHAT_INDICATION = 9;
        public static final int CHAT_MESSAGE = 10;
        public static final int REMOVE_CONTACT_REQUEST = 11;
        public static final int REMOVE_CHATS_REQUEST = 12;
        public static final int LEAVE_CHAT_INDICATION = 13;
        public static final int ONLINE_CONTACTS_INDICATION = 14;
    }
}
