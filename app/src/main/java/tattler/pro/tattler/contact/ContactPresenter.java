package tattler.pro.tattler.contact;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import tattler.pro.tattler.models.Contact;

public class ContactPresenter extends MvpBasePresenter<ContactView> {

    @SuppressWarnings("ConstantConditions")
    public void onCreate(Contact contact) {
        if (isViewAttached()) {
            getView().loadContactData(contact);
        }
    }
}
