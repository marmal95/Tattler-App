package tattler.pro.tattler.main.invitations;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import tattler.pro.tattler.R;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.common.OnItemViewClickListener;
import tattler.pro.tattler.custom_ui.MaterialToast;
import tattler.pro.tattler.main.MainActivity;

public class InvitationsFragment extends MvpFragment<InvitationsView, InvitationsPresenter>
        implements InvitationsView, OnItemViewClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView invitationsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitations, container, false);
        ButterKnife.bind(this, view);
        invitationsView.setHasFixedSize(true);
        invitationsView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @NonNull
    @Override
    public InvitationsPresenter createPresenter() {
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        return new InvitationsPresenter(
                new InvitationsAdapter(activity, new ArrayList<>(), this),
                OpenHelperManager.getHelper(activity, DatabaseManager.class),
                activity.getPresenter());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy();
    }

    @Override
    public void setInvitationsAdapter(InvitationsAdapter adapter) {
        invitationsView.setAdapter(adapter);
    }

    @Override
    public void onItemViewClick(int position, View view) {
        if (view.getId() == R.id.acceptChat) {
            MaterialToast.makeText(getContext(), "CLICK", Toast.LENGTH_LONG, MaterialToast.TYPE_SUCCESS).show();
        } else {
            MaterialToast.makeText(getContext(), "CLICK", Toast.LENGTH_LONG, MaterialToast.TYPE_ERROR).show();
        }
    }
}
