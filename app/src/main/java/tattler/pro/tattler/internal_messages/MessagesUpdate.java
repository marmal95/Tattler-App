package tattler.pro.tattler.internal_messages;

import java.util.ArrayList;
import java.util.List;

import tattler.pro.tattler.models.Message;

public class MessagesUpdate extends InternalMessage {
    private static final long serialVersionUID = 5392538539633075765L;
    public List<Message> messages;
    public Reason reason;

    public MessagesUpdate() {
        super(Type.MESSAGES_UPDATE);
        messages = new ArrayList<>();
    }

    public enum Reason {
        NEW_MESSAGE_RECEIVED
    }
}
