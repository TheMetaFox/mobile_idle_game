package com.example.idle_game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button GenerateStuffButton;
    TextView StuffAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GenerateStuffButton = findViewById(R.id.generate_stuff_button);
        StuffAmountTextView = findViewById(R.id.stuff_amount_textview);

        GenerateStuffButton.setOnClickListener(v -> {
            int stuffAmount = (int) StuffAmountTextView.getAlpha();
            stuffAmount++;
            StuffAmountTextView.setText(stuffAmount);
        });
    }
}