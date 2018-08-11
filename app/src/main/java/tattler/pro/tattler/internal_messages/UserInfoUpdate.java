package tattler.pro.tattler.internal_messages;

public class UserInfoUpdate extends InternalMessage {
    private static final long serialVersionUID = -4401474931338544646L;
    public int userPhoneId;
    public String userName;
    public Reason reason;

    public UserInfoUpdate() {
        super(Type.USER_INFO_UPDATE);
    }

    public enum Reason {
        LOGIN_SUCCESSFUL
    }
}
