package tattler.pro.tattler.main.contacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import tattler.pro.tattler.R;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.main.MainActivity;

import java.util.ArrayList;

public class ContactsFragment extends MvpFragment<ContactsView, ContactsPresenter> implements ContactsView {
    @BindView(R.id.recyclerView)
    RecyclerView contactsView;

    @BindView(R.id.addContactFab)
    FloatingActionButton addContactsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, view);
        contactsView.setHasFixedSize(true);
        contactsView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @NonNull
    @Override
    public ContactsPresenter createPresenter() {
        return new ContactsPresenter(
                new ContactsAdapter(getActivity(), new ArrayList<>()),
                OpenHelperManager.getHelper(getActivity(), DatabaseManager.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy();
    }

    @OnClick(R.id.addContactFab)
    public void addContactsButtonClick() {
        getPresenter().handleAddContactButtonClick();
    }

    @Override
    public void setContactsAdapter(ContactsAdapter adapter) {
        contactsView.setAdapter(adapter);
    }

    @Override
    public void startAddContactDialog() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            new AddContactDialog(activity, getPresenter()).show();
        }
    }
}
