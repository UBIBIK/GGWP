package com.example.guardiango.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guardiango.R;

public class find_id extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id);

        Button find_id_cancel = (Button) findViewById(R.id.find_id_cancel);
        find_id_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent_find = new Intent(getApplicationContext(), select_find.class);
                startActivity(intent_find);
            }
        });
    }
}
