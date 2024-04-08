package com.example.guardiango;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent_login = new Intent(getApplicationContext(), your_new_home.class);
                startActivity(intent_login);
            }
        });

        Button sign_up = (Button) findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent_sing_up = new Intent(getApplicationContext(), sign_up_main.class);
                startActivity(intent_sing_up);
            }
        });

        Button find = (Button) findViewById(R.id.find_id_button);
        find.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent_find = new Intent(getApplicationContext(), select_find.class);
                startActivity(intent_find);
            }
        });
    }
}