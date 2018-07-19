package tattler.pro.tattler.main.contacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tattler.pro.tattler.R;
import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.common.IntentKey;
import tattler.pro.tattler.common.OnItemClickListener;
import tattler.pro.tattler.contact.ContactActivity;
import tattler.pro.tattler.main.MainActivity;
import tattler.pro.tattler.models.Contact;

public class ContactsFragment extends MvpFragment<ContactsView, ContactsPresenter>
        implements ContactsView, OnItemClickListener {
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
        setUpContactsView();
        return view;
    }

    @NonNull
    @Override
    public ContactsPresenter createPresenter() {
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        return new ContactsPresenter(
                new ContactsAdapter(getActivity(), new ArrayList<>(), this),
                OpenHelperManager.getHelper(getActivity(), DatabaseManager.class),
                activity.getPresenter(),
                AppPreferences.getInstance(getActivity()));
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

    @Override
    public void startContactActivity(Contact contact, int position) {
        FragmentActivity activity = getActivity();
        assert activity != null;

        View clickedView = contactsView.getChildAt(position);

        Intent intent = new Intent(activity, ContactActivity.class);
        intent.putExtra(IntentKey.CONTACT.name(), contact);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, Pair.create(clickedView.findViewById(R.id.userAvatar), getString(R.string.contactAvatar)), Pair.create(clickedView.findViewById(R.id.userName), getString(R.string.contactName)), Pair.create(clickedView.findViewById(R.id.userNumber), getString(R.string.contactNumber)));

        startActivity(intent, options.toBundle());
    }

    @Override
    public void onItemClick(int position) {
        getPresenter().handleContactClick(position);
    }

    private void setUpContactsView() {
        Context context = getContext();
        assert context != null;
        Drawable dividerDrawable = ContextCompat.getDrawable(context, R.drawable.recycler_view_divider);
        assert dividerDrawable != null;

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(dividerDrawable);
        contactsView.setHasFixedSize(true);
        contactsView.addItemDecoration(dividerItemDecoration);
        contactsView.setLayoutManager(new LinearLayoutManager(context));
    }
}
