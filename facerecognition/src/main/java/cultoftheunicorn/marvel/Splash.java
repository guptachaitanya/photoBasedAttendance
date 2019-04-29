package cultoftheunicorn.marvel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.opencv.cultoftheunicorn.marvel.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences pre = getSharedPreferences("AuthPre",MODE_PRIVATE);
                boolean b = pre.getBoolean("auth",false);
                if (b) {
                    Intent in = new Intent(Splash.this, HomeActivity.class);
                    startActivity(in);
                    finish();
                }
                else{
                    Intent in = new Intent(Splash.this, WelcomeActivity.class);
                    startActivity(in);
                    finish();
                }
            }

        },3000);
    }
}
