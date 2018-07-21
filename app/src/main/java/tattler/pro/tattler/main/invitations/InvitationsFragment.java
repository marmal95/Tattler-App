package tattler.pro.tattler.main.invitations;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import tattler.pro.tattler.R;

public class InvitationsFragment extends MvpFragment<InvitationsView, InvitationsPresenter> {

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
        return new InvitationsPresenter();
    }
}