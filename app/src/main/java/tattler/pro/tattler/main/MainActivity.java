package tattler.pro.tattler.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import tattler.pro.tattler.R;
import tattler.pro.tattler.tcp.TcpConnectionService;
import tattler.pro.tattler.tcp.TcpServiceConnector;
import tattler.pro.tattler.tcp.TcpServiceConnectorFactory;
import tattler.pro.tattler.tcp.TcpServiceManager;

import java.util.Objects;

public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigationView)
    NavigationView navigationView;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setNavigationItemSelectedListener();
        setUpToolbar();
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
                new TcpServiceConnectorFactory());
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
    public void bindTcpConnectionService(TcpServiceConnector serviceConnector) {
        Intent intent = new Intent(this, TcpConnectionService.class);
        startService(intent);
        bindService(intent, serviceConnector, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void unbindTcpConnectionService(TcpServiceConnector serviceConnector) {
        unbindService(serviceConnector);
    }

    private void setNavigationItemSelectedListener() {
        navigationView.setNavigationItemSelectedListener(item -> {
            item.setCheckable(true);
            drawerLayout.closeDrawers();
            return true;
        });
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
