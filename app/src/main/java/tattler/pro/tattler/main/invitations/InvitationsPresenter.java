package tattler.pro.tattler.main.invitations;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.security.KeyPair;
import java.sql.SQLException;
import java.util.List;

import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.main.MainPresenter;
import tattler.pro.tattler.messages.ChatInvitationResponse;
import tattler.pro.tattler.messages.MessageFactory;
import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Invitation;
import tattler.pro.tattler.security.RsaCrypto;

public class InvitationsPresenter extends MvpBasePresenter<InvitationsView> {
    private InvitationsAdapter invitationsAdapter;
    private DatabaseManager databaseManager;
    private MainPresenter mainPresenter;
    private MessageFactory messageFactory;
    private RsaCrypto rsaCrypto;

    InvitationsPresenter(
            InvitationsAdapter
                    invitationsAdapter,
            DatabaseManager databaseManager,
            MainPresenter mainPresenter,
            MessageFactory messageFactory,
            RsaCrypto rsaCrypto) {
        this.invitationsAdapter = invitationsAdapter;
        this.databaseManager = databaseManager;
        this.mainPresenter = mainPresenter;
        this.mainPresenter.setInvitationsPresenter(this);
        this.messageFactory = messageFactory;
        this.rsaCrypto = rsaCrypto;
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
        KeyPair keyPair = rsaCrypto.generateRsaKeyPair();
        byte[] publicKey = keyPair.getPublic().getEncoded();
        byte[] privateKey = keyPair.getPublic().getEncoded();

        Invitation invitation = invitationsAdapter.getInvitation(position);
        Chat chat = invitation.chat;

        updateChatKeys(chat, publicKey, privateKey);
        databaseManager.updateChat(chat);

        sendChatInvitationResponse(invitation, publicKey);
        changeInvitationStateToPendingReactionResponse(position);
    }

    private void updateChatKeys(Chat chat, byte[] publicKey, byte[] privateKey) {
        chat.publicKey = publicKey;
        chat.privateKey = privateKey;
    }

    private void sendChatInvitationResponse(Invitation invitation, byte[] publicKey) {
        ChatInvitationResponse invitationResponse = messageFactory.createChatInvitationResponse(
                invitation,
                ChatInvitationResponse.Status.INVITATION_ACCEPTED,
                publicKey);
        mainPresenter.sendMessage(invitationResponse);
    }

    @SuppressWarnings("ConstantConditions")
    private void changeInvitationStateToPendingReactionResponse(int position) {
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
