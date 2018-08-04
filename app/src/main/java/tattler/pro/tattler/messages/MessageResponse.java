package tattler.pro.tattler.messages;

public abstract class MessageResponse extends Message {
    private static final long serialVersionUID = -8056427031966310865L;
    public long acknowledgedMessageId;
    public int status;

    public MessageResponse(int messageType, long acknowledgedMessageId, int status) {
        super(messageType);
        this.acknowledgedMessageId = acknowledgedMessageId;
        this.status = status;
    }
}
