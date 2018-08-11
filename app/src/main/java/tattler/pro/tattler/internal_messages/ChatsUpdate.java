package tattler.pro.tattler.internal_messages;

import java.util.ArrayList;
import java.util.List;

import tattler.pro.tattler.models.Chat;

public class ChatsUpdate extends InternalMessage {
    private static final long serialVersionUID = -1065561689141914156L;
    public List<Chat> chats;
    public Reason reason;

    public ChatsUpdate() {
        super(Type.CHATS_UPDATE);
        chats = new ArrayList<>();
    }

    public enum Reason {
        NEW_CHAT_CREATED,
    }
}
