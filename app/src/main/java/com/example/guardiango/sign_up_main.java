package com.example.guardiango;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class sign_up_main extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_main);

        EditText editTextEmail = findViewById(R.id.editTextUserID);
        Button buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = editTextEmail.getText().toString().trim();

                if (Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    // 이메일 형식이 유효할 때의 로직
                    Toast.makeText(sign_up_main.this, "올바른 이메일 주소.", Toast.LENGTH_SHORT).show();
                    // 예: Intent를 사용해 다른 액티비티로 이동
                } else {
                    // 이메일 형식이 유효하지 않을 때 사용자에게 피드백 제공
                    Toast.makeText(sign_up_main.this, "유효한 이메일 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button back = findViewById(R.id.sign_up_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sign_up_main.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
