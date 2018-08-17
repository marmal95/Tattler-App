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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tattler.pro.tattler.R;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.common.IntentKey;
import tattler.pro.tattler.common.OnItemClickListener;
import tattler.pro.tattler.contact.ContactActivity;
import tattler.pro.tattler.custom_ui.MaterialToast;
import tattler.pro.tattler.main.MainActivity;
import tattler.pro.tattler.messages.MessageFactory;
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
                new ContactsAdapter(activity, new ArrayList<>(), this),
                OpenHelperManager.getHelper(activity, DatabaseManager.class),
                activity.getPresenter(),
                new MessageFactory(activity));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.contacts_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contactsRemove:
                getPresenter().handleContactsRemoveClick();
                break;
        }
        return super.onOptionsItemSelected(item);
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
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                Pair.create(clickedView.findViewById(R.id.userAvatar), getString(R.string.contactAvatar)),
                Pair.create(clickedView.findViewById(R.id.userName), getString(R.string.contactName)),
                Pair.create(clickedView.findViewById(R.id.userNumber), getString(R.string.contactNumber)));

        startActivity(intent, options.toBundle());
    }

    @Override
    public void showContactAddingError() {
        MaterialToast.makeText(getActivity(), getString(R.string.contactCreatingError),
                Toast.LENGTH_LONG, MaterialToast.TYPE_ERROR).show();
    }

    @Override
    public void showContactAlreadyAddedInfo() {
        MaterialToast.makeText(getActivity(), getString(R.string.contactAlreadyAddedInfo),
                Toast.LENGTH_LONG, MaterialToast.TYPE_WARNING).show();
    }

    @Override
    public void showContactNotExistInfo() {
        MaterialToast.makeText(getActivity(), getString(R.string.contactNotExist),
                Toast.LENGTH_LONG, MaterialToast.TYPE_WARNING).show();
    }

    @Override
    public void onItemClick(int position) {
        getPresenter().handleContactClick(position);
    }

    @Override
    public boolean onItemLongClick(int position) {
        getPresenter().handleContactLongClick(position);
        return true;
    }

    private void setUpContactsView() {
        Context context = getContext();
        Drawable dividerDrawable = ContextCompat.getDrawable(Objects.requireNonNull(context), R.drawable.recycler_view_divider);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(Objects.requireNonNull(dividerDrawable));
        contactsView.setHasFixedSize(true);
        contactsView.addItemDecoration(dividerItemDecoration);
        contactsView.setLayoutManager(layoutManager);
    }
}
