package tattler.pro.tattler.main.invitations;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import tattler.pro.tattler.common.ChatsManager;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.main.MainPresenter;
import tattler.pro.tattler.messages.ChatInvitationResponse;
import tattler.pro.tattler.messages.MessageFactory;
import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Invitation;

public class InvitationsPresenter extends MvpBasePresenter<InvitationsView> {
    private InvitationsAdapter invitationsAdapter;
    private DatabaseManager databaseManager;
    private MainPresenter mainPresenter;
    private MessageFactory messageFactory;
    private ChatsManager chatsManager;

    InvitationsPresenter(
            InvitationsAdapter
                    invitationsAdapter,
            DatabaseManager databaseManager,
            MainPresenter mainPresenter,
            MessageFactory messageFactory,
            ChatsManager chatsManager) {
        this.invitationsAdapter = invitationsAdapter;
        this.databaseManager = databaseManager;
        this.mainPresenter = mainPresenter;
        this.mainPresenter.setInvitationsPresenter(this);
        this.messageFactory = messageFactory;
        this.chatsManager = chatsManager;
        this.chatsManager.setDatabaseManager(databaseManager);
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
        sendChatInvitationResponse(invitation, ChatInvitationResponse.Status.INVITATION_ACCEPTED, invitation.chat.publicKey);
        updateInvitationStateToPendingResponse(invitation);
    }

    public void handleRejectInvitation(int position) {
        Invitation invitation = invitationsAdapter.getInvitation(position);
        sendChatInvitationResponse(invitation, ChatInvitationResponse.Status.INVITATION_REJECTED, null);
        invitationsAdapter.removeInvitation(position);
        removeChat(invitation);
    }

    public void handleInvitationReceived(Invitation invitation) {
        invitationsAdapter.addInvitation(invitation);
    }

    public void handleChatInitializedIndication(List<Invitation> invitations) {
        invitationsAdapter.removeInvitations(invitations);
    }

    public void handleChatRemoved(Chat chat) {
        invitationsAdapter.removeFor(chat);
    }

    private void sendChatInvitationResponse(Invitation invitation, int status, byte[] publicKey) {
        ChatInvitationResponse invitationResponse = messageFactory.createChatInvitationResponse(
                invitation,
                status,
                publicKey);
        mainPresenter.sendMessage(invitationResponse);
    }

    private void updateInvitationStateToPendingResponse(Invitation invitation) {
        try {
            invitation.state = Invitation.State.PENDING_FOR_RESPONSE;
            databaseManager.updateInvitation(invitation);
        } catch (SQLException e) {
            e.printStackTrace();
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

    private void removeChat(Invitation invitation) {
        chatsManager.removeChat(invitation.chat);
    }
}
