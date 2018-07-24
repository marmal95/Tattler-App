package tattler.pro.tattler.main.invitations;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.main.MainPresenter;
import tattler.pro.tattler.models.Invitation;

public class InvitationsPresenter extends MvpBasePresenter<InvitationsView> {
    private InvitationsAdapter invitationsAdapter;
    private DatabaseManager databaseManager;
    private MainPresenter mainPresenter;

    InvitationsPresenter(InvitationsAdapter invitationsAdapter, DatabaseManager databaseManager, MainPresenter mainPresenter) {
        this.invitationsAdapter = invitationsAdapter;
        this.databaseManager = databaseManager;
        this.mainPresenter = mainPresenter;
        this.mainPresenter.setInvitationsPresenter(this);
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
