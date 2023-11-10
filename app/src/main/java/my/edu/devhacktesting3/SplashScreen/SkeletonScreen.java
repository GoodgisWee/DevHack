package my.edu.devhacktesting3.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import my.edu.devhacktesting3.Login.Login;
import my.edu.devhacktesting3.R;
import my.edu.devhacktesting3.MainActivity;

public class SkeletonScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skeleton_screen);

        // Delay for 4 seconds (4000 milliseconds)
        int delayMillis = 4000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an intent to launch the MainActivity
                Intent intent = new Intent(SkeletonScreen.this, Login.class);
                startActivity(intent);

                // Finish the SkeletonScreen activity to prevent going back to it
                finish();
            }
        }, delayMillis);
    }
}
