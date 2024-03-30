package com.example.guardiango;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class select_find extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_find);

        Button select_find_id = (Button) findViewById(R.id.select_find_id);
        select_find_id.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent_find = new Intent(getApplicationContext(), find_id.class);
                startActivity(intent_find);
            }
        });

        Button select_find_password = (Button) findViewById(R.id.select_find_password);
        select_find_password.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent_find = new Intent(getApplicationContext(), find_password.class);
                startActivity(intent_find);
            }
        });

        Button select_find_cancel = (Button) findViewById(R.id.select_find_cancel);
        select_find_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent_find = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_find);
            }
        });
    }
}
