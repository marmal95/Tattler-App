package tattler.pro.tattler.messages;

import java.util.Arrays;

public class ChatInvitationResponse extends MessageResponse {
    private static final long serialVersionUID = 1255333954342088918L;
    public int chatId;
    public byte[] publicKey;

    public ChatInvitationResponse(int senderId, int receiverId, long acknowledgedMessageId, int status) {
        super(Type.CHAT_INVITATION_RESPONSE, acknowledgedMessageId, status);
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    @Override
    public String toString() {
        return "ChatInvitationResponse{" +
                "chatId=" + chatId +
                ", publicKey=" + Arrays.toString(publicKey) +
                ", acknowledgedMessageId=" + acknowledgedMessageId +
                ", status=" + status +
                ", messageType=" + messageType +
                ", messageTime=" + messageTime +
                ", messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }

    public class Status {
        public static final int INVITATION_ACCEPTED = 0;
    }
}
