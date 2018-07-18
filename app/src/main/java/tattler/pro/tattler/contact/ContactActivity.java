package tattler.pro.tattler.contact;

import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import tattler.pro.tattler.R;

public class ContactActivity extends MvpActivity<ContactView, ContactPresenter> {
    @BindView(R.id.userAvatar)
    AvatarView userAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

        PicassoLoader picassoLoader = new PicassoLoader();
        picassoLoader.loadImage(userAvatar, (String) null, "");
    }

    @NonNull
    @Override
    public ContactPresenter createPresenter() {
        return new ContactPresenter();
    }
}
