package com.example.guardiango.screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guardiango.R;

public class find_password extends AppCompatActivity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_password);

        EditText editTextEmail = findViewById(R.id.find_password_email);
        Button find_password_check = findViewById(R.id.find_password_check);

        find_password_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = editTextEmail.getText().toString().trim();

                if (Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    // 이메일 형식이 유효할 때의 로직
                    Toast.makeText(find_password.this, "올바른 이메일 주소.", Toast.LENGTH_SHORT).show();
                    // 예: Intent를 사용해 다른 액티비티로 이동
                } else {
                    // 이메일 형식이 유효하지 않을 때 사용자에게 피드백 제공
                    Toast.makeText(find_password.this, "유효한 이메일 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button find_password_cancel = (Button) findViewById(R.id.find_password_cancel);
        find_password_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent_find = new Intent(getApplicationContext(), select_find.class);
                startActivity(intent_find);
            }
        });
    }
}
