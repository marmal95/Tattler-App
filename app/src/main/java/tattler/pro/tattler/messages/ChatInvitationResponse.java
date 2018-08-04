package tattler.pro.tattler.messages;

public class ChatInvitationResponse extends MessageResponse {
    private static final long serialVersionUID = 1255333954342088918L;
    public byte[] publicKey;

    public ChatInvitationResponse(long acknowledgedMessageId, int status) {
        super(Type.CHAT_INVITATION_RESPONSE, acknowledgedMessageId, status);
    }

    public class Status {
        public static final int INVITATION_ACCEPTED = 0;
    }
}
