package tattler.pro.tattler.messages;

public class LeaveChatIndication extends Message {
    private static final long serialVersionUID = 1043953621720582702L;
    public int leavingUserNumber;
    public int chatId;

    public LeaveChatIndication(int receiverId, int chatId, int leavingUserNumber) {
        super(Type.LEAVE_CHAT_INDICATION);
        this.leavingUserNumber = leavingUserNumber;
        this.receiverId = receiverId;
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "LeaveChatIndication{" +
                "leavingUserNumber=" + leavingUserNumber +
                ", chatId=" + chatId +
                ", messageType=" + messageType +
                ", messageTime=" + messageTime +
                ", messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}
