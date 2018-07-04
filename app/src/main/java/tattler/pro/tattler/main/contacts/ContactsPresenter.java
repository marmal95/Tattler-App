package tattler.pro.tattler.main.contacts;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

public class ContactsPresenter extends MvpBasePresenter<ContactsView> {
    private ContactsAdapter contactsAdapter;

    public ContactsPresenter(ContactsAdapter contactsAdapter) {
        this.contactsAdapter = contactsAdapter;
        setContactsAdapter();
    }

    @SuppressWarnings("ConstantConditions")
    private void setContactsAdapter() {
        if (isViewAttached()) {
            getView().setContactsAdapter(contactsAdapter);
        }
    }
}
