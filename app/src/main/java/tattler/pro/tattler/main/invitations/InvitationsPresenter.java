package tattler.pro.tattler.main.invitations;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.main.MainPresenter;
import tattler.pro.tattler.messages.ChatInvitationResponse;
import tattler.pro.tattler.messages.MessageFactory;
import tattler.pro.tattler.models.Invitation;

public class InvitationsPresenter extends MvpBasePresenter<InvitationsView> {
    private InvitationsAdapter invitationsAdapter;
    private DatabaseManager databaseManager;
    private MainPresenter mainPresenter;
    private MessageFactory messageFactory;

    InvitationsPresenter(
            InvitationsAdapter
                    invitationsAdapter,
            DatabaseManager databaseManager,
            MainPresenter mainPresenter,
            MessageFactory messageFactory) {
        this.invitationsAdapter = invitationsAdapter;
        this.databaseManager = databaseManager;
        this.mainPresenter = mainPresenter;
        this.mainPresenter.setInvitationsPresenter(this);
        this.messageFactory = messageFactory;
    }

    @Override
    public void attachView(InvitationsView view) {
        super.attachView(view);
        view.setInvitationsAdapter(invitationsAdapter);
        initInvitations();
    }

    void onDestroy() {
        OpenHelperManager.releaseHelper();
    }

    public void handleAcceptInvitation(int position) {
        Invitation invitation = invitationsAdapter.getInvitation(position);
        sendChatInvitationResponse(invitation, invitation.chat.publicKey);
        updateInvitationStateToPendingResponse(invitation);
        changeInvitationStateToPendingResponse(position);
    }

    public void handleInvitationReceived(Invitation invitation) {
        invitationsAdapter.addInvitation(invitation);
    }

    private void sendChatInvitationResponse(Invitation invitation, byte[] publicKey) {
        ChatInvitationResponse invitationResponse = messageFactory.createChatInvitationResponse(
                invitation,
                ChatInvitationResponse.Status.INVITATION_ACCEPTED,
                publicKey);
        mainPresenter.sendMessage(invitationResponse);
    }

    private void updateInvitationStateToPendingResponse(Invitation invitation) {
        try {
            invitation.state = Invitation.State.PENDING_FOR_RESPONSE;
            databaseManager.insertInvitation(invitation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void changeInvitationStateToPendingResponse(int position) {
        if (isViewAttached()) {
            getView().changeInvitationToPending(position);
        }
    }

    private void initInvitations() {
        try {
            List<Invitation> invitations = databaseManager.selectInvitations();
            invitationsAdapter.clearInvitations();
            invitationsAdapter.addInvitations(invitations);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
