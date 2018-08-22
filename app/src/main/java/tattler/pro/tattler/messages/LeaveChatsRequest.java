package tattler.pro.tattler.messages;

import java.util.List;

public class LeaveChatsRequest extends Message {
    private static final long serialVersionUID = 4210759302679070257L;
    public List<Integer> chatsId;

    public LeaveChatsRequest(List<Integer> chatsId, int senderId) {
        super(Type.REMOVE_CHATS_REQUEST);
        this.chatsId = chatsId;
        this.senderId = senderId;
    }

    @Override
    public String toString() {
        return "LeaveChatsRequest{" +
                "chatsId=" + chatsId +
                ", messageType=" + messageType +
                ", messageTime=" + messageTime +
                ", messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}
