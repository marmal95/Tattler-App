package tattler.pro.tattler.internal_messages;

import java.io.Serializable;

public abstract class InternalMessage implements Serializable {
    private static final long serialVersionUID = -8268678857108676445L;
    public Type type;

    InternalMessage(Type type) {
        this.type = type;
    }

    public enum Type {
        USER_INFO_UPDATE,
        CONTACTS_UPDATE,
        CHATS_UPDATE,
        INVITATIONS_UPDATE,
        MESSAGES_UPDATE
    }
}
