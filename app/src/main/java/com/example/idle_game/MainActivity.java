package com.example.idle_game;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    DBHelper DBHelper;

    Button GenerateStuffButton;
    Button FirstGenerationButton;
    TextView StuffAmountTextView;
    TextView FirstGenerationLevelTextView;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int step = DBHelper.getLevel(1);
            while(true) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DBHelper.updateStuff(step);
                        StuffAmountTextView = findViewById(R.id.stuff_amount_textview);
                        StuffAmountTextView.setText(String.valueOf(DBHelper.getStuff()));
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper = new DBHelper(MainActivity.this);

        GenerateStuffButton = findViewById(R.id.generate_stuff_button);
        FirstGenerationButton = findViewById(R.id.first_generation_button);
        StuffAmountTextView = findViewById(R.id.stuff_amount_textview);
        FirstGenerationLevelTextView = findViewById(R.id.first_generation_level_textview);

        //TimerThread Thread = new TimerThread(MainActivity.this);
        //Thread.start();
        Thread thread = new Thread(runnable);
        thread.start();

        GenerateStuffButton.setOnClickListener(v -> {
            DBHelper.updateStuff(1);
            StuffAmountTextView.setText(String.valueOf(DBHelper.getStuff()));
        });

        FirstGenerationButton.setOnClickListener(v -> {
            DBHelper.levelUp(1);
            FirstGenerationLevelTextView.setText(String.valueOf(DBHelper.getLevel(1)));
        });
    }
}