package ah.production.dileverybot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wang.avi.AVLoadingIndicatorView;

public class SplashActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView logo_name;
    private TextView logo_intro;
    private AVLoadingIndicatorView avi;

    private final int SPLASH_DISPLAY_LENGTH = 5000;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.iv_logo);
        logo_name = findViewById(R.id.logo_name);
        logo_intro = findViewById(R.id.logo_intro);
        avi = findViewById(R.id.avi);

        logo.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        logo_name.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_more1));
        logo_intro.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down));
        avi.smoothToShow();
        firebaseAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                avi.smoothToHide();
                screenTransition();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void screenTransition() {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
        else {
            Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }
}