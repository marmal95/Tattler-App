package tattler.pro.tattler.internal_messages;

import java.util.ArrayList;
import java.util.List;

import tattler.pro.tattler.models.Invitation;

public class InvitationsUpdate extends InternalMessage {
    private static final long serialVersionUID = -6085542033002619600L;
    public List<Invitation> invitations;
    public Reason reason;

    public InvitationsUpdate() {
        super(Type.INVITATIONS_UPDATE);
        invitations = new ArrayList<>();
    }

    public enum Reason {
        NEW_INVITATION
    }
}
