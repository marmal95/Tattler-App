package tattler.pro.tattler.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import tattler.pro.tattler.login.FingerprintLoginActivity;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, FingerprintLoginActivity.class));
        finish();
    }
}