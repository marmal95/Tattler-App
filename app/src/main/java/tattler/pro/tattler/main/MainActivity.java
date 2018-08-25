package tattler.pro.tattler.main;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tattler.pro.tattler.R;
import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.common.Util;
import tattler.pro.tattler.main.chats.ChatsFragment;
import tattler.pro.tattler.main.contacts.ContactsFragment;
import tattler.pro.tattler.main.invitations.InvitationsFragment;
import tattler.pro.tattler.main.settings.SettingsFragment;
import tattler.pro.tattler.tcp.MessageBroadcastReceiver;
import tattler.pro.tattler.tcp.TcpConnectionService;
import tattler.pro.tattler.tcp.TcpServiceConnector;
import tattler.pro.tattler.tcp.TcpServiceConnectorFactory;
import tattler.pro.tattler.tcp.TcpServiceManager;

public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView, NavigationView.OnNavigationItemSelectedListener {
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
        navigationView.getMenu().getItem(0).setChecked(true);
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
                new MainMessageHandler(),
                new MessageBroadcastReceiver(), AppPreferences.getInstance(this));
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
            case R.id.navChatsFragment:
                getPresenter().handleNavChatsClick();
                break;
            case R.id.navInvitationsFragment:
                getPresenter().handleNavInvitationsClick();
                break;
            case R.id.navSettingsFragment:
                getPresenter().handleNavSettingsClick();
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
        activeFragment = new ContactsFragment();
        replaceFragment();
        changeTitle(getString(R.string.contacts));
    }

    @Override
    public void startChatsFragment() {
        activeFragment = new ChatsFragment();
        replaceFragment();
        changeTitle(getString(R.string.chats));
    }

    @Override
    public void startInvitationsFragment() {
        activeFragment = new InvitationsFragment();
        replaceFragment();
        changeTitle(getString(R.string.invitations));
    }

    @Override
    public void startSettingsFragment() {
        activeFragment = new SettingsFragment();
        replaceFragment();
        changeTitle(getString(R.string.settings));
    }

    @Override
    public void displayUserData(String userName, String userNumber) {
        View headerView = navigationView.getHeaderView(0);
        ColorDrawable userNameColorDrawable = new ColorDrawable(Color.parseColor(Util.pickHexColor(this, userName)));

        CircleImageView userAvatar = headerView.findViewById(R.id.userAvatar);
        TextView userInitials = headerView.findViewById(R.id.userInitials);
        TextView userNameArea = headerView.findViewById(R.id.userName);
        TextView userNumberArea = headerView.findViewById(R.id.userNumber);

        userAvatar.setImageDrawable(userNameColorDrawable);
        userInitials.setText(Util.extractUserInitials(userName));
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

    @Override
    public void changeTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
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

    private void replaceFragment() {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction = fragmentTransaction.replace(R.id.contentFrame, activeFragment);
        fragmentTransaction.commit();
    }
}
