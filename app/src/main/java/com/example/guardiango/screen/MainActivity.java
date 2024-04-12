package com.example.guardiango.screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.guardiango.R;
import com.example.guardiango.server.RetrofitClient;
import com.example.guardiango.server.UserRetrofitInterface;
import com.example.guardiango.entity.UserDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                EditText editEmail = findViewById(R.id.editTextUserID);
                EditText editPassword = findViewById(R.id.editTextPassword);

                RetrofitClient retrofitClient = RetrofitClient.getInstance();
                UserRetrofitInterface userRetrofitInterface = RetrofitClient.getUserRetrofitInterface();

                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                UserDTO user = new UserDTO("","",email,password);
                Call<ResponseBody> call = userRetrofitInterface.loginUser(user);

                call.clone().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Log.w("로그인","성공");
                            //화면 넘기기
                            Intent intent_login = new Intent(getApplicationContext(), your_new_home.class);
                            startActivity(intent_login);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("로그인","실패");
                    }
                });
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

        Button test = (Button) findViewById(R.id.test_button);
        test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent_find = new Intent(getApplicationContext(), your_new_home.class);
                startActivity(intent_find);
            }
        });
    }
}