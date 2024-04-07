package com.example.guardiango;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class sign_up_main extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_main);

        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPhoneNumber = findViewById(R.id.PhoneNumber);
        EditText editTextEmail = findViewById(R.id.editTextUserID);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        EditText editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
        Button buttonSignUp = findViewById(R.id.buttonSignUp);

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        UserRetrofitInterface userRetrofitInterface = RetrofitClient.getUserRetrofitInterface();

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이메일 확인 로직
                String userName = editTextUsername.getText().toString().trim();
                String Phone_number = editTextPhoneNumber.getText().toString().trim();
                String emailInput = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String passwordConfirm = editTextPasswordConfirm.getText().toString().trim();

                // 이메일 주소가 유효하지 않을 때
                if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    Toast.makeText(sign_up_main.this, "유효한 이메일 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                // 비밀번호가 일치하지 않을 때
                else if (!password.equals(passwordConfirm)) {
                    Toast.makeText(sign_up_main.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                // 모두 유효 할 때
                else {
                    Toast.makeText(sign_up_main.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();

                    UserDTO userDTo = new UserDTO(userName, Phone_number, emailInput, password);
                    Log.w("정보확인", userDTo.toString());
                    Gson gson = new Gson();
                    String userInfo = gson.toJson(userDTo);

                    Log.e("JSON", userInfo);

                    Call<ResponseBody> call = userRetrofitInterface.saveUser(userDTo);
                    call.clone().enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Log.w("회원가입", "성공");
                                //화면 넘어가는 로직 구현하기
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("POST", "실패");
                        }
                    });
                }


            }
        });

        // 뒤로가기 버튼
        Button back = findViewById(R.id.sign_up_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
