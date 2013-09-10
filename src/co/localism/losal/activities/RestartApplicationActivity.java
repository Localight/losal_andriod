package co.localism.losal.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/** This activity shows nothing; instead, it restarts the android process */
public class RestartApplicationActivity extends Activity {
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.exit(0);
    }
    public static void doRestart(Activity anyActivity) {
        anyActivity.startActivity(new Intent(anyActivity.getApplicationContext(), RestartApplicationActivity.class));
    }
}