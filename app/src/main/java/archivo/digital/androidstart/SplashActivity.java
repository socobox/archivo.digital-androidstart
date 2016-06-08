package archivo.digital.androidstart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import archivo.digital.androidstart.R;

/**
 * @author https://archivo.digital
 *         Created by miguel@archivo.digital 8/06/2016.
 */
public class SplashActivity extends PlaceHolderActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
               startActivity(new Intent(self, LoginActivity.class));
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
