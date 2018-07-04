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
import tattler.pro.tattler.R;

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
                new ContactsAdapter<ContactsAdapter.ViewHolder>(getActivity(), new ArrayList<>()));
    }

    @OnClick(R.id.addContactFab)
    public void addContactsButtonClick() {

    }

    @Override
    public void setContactsAdapter(ContactsAdapter adapter) {
        contactsView.setAdapter(adapter);
    }
}
