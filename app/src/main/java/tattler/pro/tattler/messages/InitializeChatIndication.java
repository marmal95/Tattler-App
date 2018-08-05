package tattler.pro.tattler.messages;

import java.util.Arrays;

public class InitializeChatIndication extends Message {
    private static final long serialVersionUID = 7240759058528440664L;
    public int chatId;
    public byte[] chatEncryptedKey;

    public InitializeChatIndication(int senderId, int receiverId) {
        super(Type.INITIALIZE_CHAT_INDICATION);
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    @Override
    public String toString() {
        return "InitializeChatIndication{" +
                "chatId=" + chatId +
                ", chatEncryptedKey=" + Arrays.toString(chatEncryptedKey) +
                ", messageType=" + messageType +
                ", messageTime=" + messageTime +
                ", messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}
