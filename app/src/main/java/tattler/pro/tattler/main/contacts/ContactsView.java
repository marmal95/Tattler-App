package tattler.pro.tattler.main.contacts;

import com.hannesdorfmann.mosby.mvp.MvpView;

import tattler.pro.tattler.models.Contact;

interface ContactsView extends MvpView {
    void setContactsAdapter(ContactsAdapter adapter);
    void startAddContactDialog();
    void startContactActivity(Contact contact, int position);
}
