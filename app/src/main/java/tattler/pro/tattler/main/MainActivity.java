package tattler.pro.tattler.main;

import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import tattler.pro.tattler.R;
import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.main.contacts.ContactsFragment;
import tattler.pro.tattler.tcp.*;

import java.util.Objects;

public class MainActivity extends MvpActivity<MainView, MainPresenter>
        implements MainView, NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigationView)
    NavigationView navigationView;

    private ActionBarDrawerToggle drawerToggle;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navigationView.setNavigationItemSelectedListener(this);
        setUpToolbar();
        startContactsFragment();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
        getPresenter().onCreate();
    }

    @Override
    protected void onDestroy() {
        getPresenter().onDestroy();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter(
                new TcpServiceManager(),
                new TcpServiceConnectorFactory(),
                AppPreferences.getInstance(this),
                new MessageBroadcastReceiver());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navContactsFragment:
                getPresenter().handleNavContactsClick();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void bindTcpConnectionService(TcpServiceConnector serviceConnector) {
        Intent intent = new Intent(this, TcpConnectionService.class);
        startService(intent);
        bindService(intent, serviceConnector, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void unbindTcpConnectionService(TcpServiceConnector serviceConnector) {
        unbindService(serviceConnector);
    }

    @Override
    public void startContactsFragment() {
        FragmentTransaction fragmentTransaction;
        activeFragment = new ContactsFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction = fragmentTransaction.replace(R.id.contentFrame, activeFragment);
        fragmentTransaction = fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void displayUserData(String userName, String userNumber) {
        View headerView = navigationView.getHeaderView(0);
        AvatarView userAvatar = headerView.findViewById(R.id.userAvatar);
        TextView userNameArea = headerView.findViewById(R.id.userName);
        TextView userNumberArea = headerView.findViewById(R.id.userNumber);

        PicassoLoader picassoLoader = new PicassoLoader();
        picassoLoader.loadImage(userAvatar, (String) null, userName);
        userNameArea.setText(userName);
        userNumberArea.setText(userNumber);
    }

    @Override
    public void registerReceiver(MessageBroadcastReceiver receiver) {
        super.registerReceiver(receiver, receiver.createBroadcastMessageIntentFilter());
    }

    @Override
    public void unregisterReceiver(MessageBroadcastReceiver receiver) {
        super.unregisterReceiver(receiver);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerToggle = setupDrawerToggle();
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
    }
}
