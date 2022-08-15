package com.example.idle_game;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ActivityState";
    LocalDateTime closeDateTime;
    LocalDateTime openDateTime;

    boolean activityVisible;
    boolean threadActive = false;

    DBHelper DBHelper;

    Button GenerateStuffButton;
    Button FirstGenerationButton;
    Button ResetStatsButton;
    TextView StuffAmountTextView;
    TextView FirstGenerationLevelTextView;

    Runnable runnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            while(activityVisible) {
                threadActive = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int step = DBHelper.getLevel(1);
                        DBHelper.updateStuff(step);
                        StuffAmountTextView = findViewById(R.id.stuff_amount_textview);
                        StuffAmountTextView.setText(String.valueOf(DBHelper.getStuff()));
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
            threadActive = false;
            Log.d(TAG, String.valueOf(Thread.activeCount()));
            //Log.i(TAG, String.valueOf(openDateTime.minus(closeDateTime.getHour(), ChronoUnit.HOURS)));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate");


        DBHelper = new DBHelper(MainActivity.this);

        GenerateStuffButton = findViewById(R.id.generate_stuff_button);
        FirstGenerationButton = findViewById(R.id.first_generation_button);
        ResetStatsButton = findViewById(R.id.reset_button);
        StuffAmountTextView = findViewById(R.id.stuff_amount_textview);
        FirstGenerationLevelTextView = findViewById(R.id.first_generation_level_textview);

        FirstGenerationLevelTextView.setText(String.valueOf(DBHelper.getLevel(1)));


        GenerateStuffButton.setOnClickListener(v -> {
            DBHelper.updateStuff(1);
            StuffAmountTextView.setText(String.valueOf(DBHelper.getStuff()));
        });

        FirstGenerationButton.setOnClickListener(v -> {
            if (DBHelper.getStuff() < 10) {
                Context context = getApplicationContext();
                CharSequence text = "Not Enough Stuff!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            DBHelper.levelUp(1);
            DBHelper.updateStuff(-10);
            FirstGenerationLevelTextView.setText(String.valueOf(DBHelper.getLevel(1)));
            StuffAmountTextView.setText(String.valueOf(DBHelper.getStuff()));

        });

        ResetStatsButton.setOnClickListener(v -> {
            DBHelper.resetData();
            StuffAmountTextView.setText("0");
            FirstGenerationLevelTextView.setText("0");
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(TAG, "onStart");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();

        activityVisible = true;
        openDateTime = LocalDateTime.now();

        if (!threadActive) {
            Thread thread = new Thread(runnable);
            thread.start();
        }

        Log.i(TAG, "onResume :: activityVisible = true");
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStop() {
        super.onStop();

        activityVisible = false;
        closeDateTime = LocalDateTime.now();

        Log.i(TAG, "onStop :: activityVisible = false");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i(TAG, "onRestart");
    }
}