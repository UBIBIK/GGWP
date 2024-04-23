package com.example.guardiango.screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.guardiango.R;
import com.example.guardiango.entity.Group;
import com.example.guardiango.entity.SharedPreferencesGroup;
import com.example.guardiango.entity.UserInfo;
import com.example.guardiango.server.RetrofitClient;
import com.example.guardiango.entity.SharedPreferencesHelper;
import com.example.guardiango.server.UserRetrofitInterface;
import com.example.guardiango.entity.UserDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private SharedPreferencesHelper sharedPreferencesHelper;
    private SharedPreferencesGroup sharedPreferencesGroup;

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

        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText editEmail = findViewById(R.id.editTextUserID);
                EditText editPassword = findViewById(R.id.editTextPassword);

                RetrofitClient.getInstance();
                UserRetrofitInterface userRetrofitInterface = RetrofitClient.getUserRetrofitInterface();

                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                UserDTO user = new UserDTO("","",email,password);

                Call<UserInfo> call = userRetrofitInterface.loginUser(user);
                Log.w("로그인 - 이메일 입력", email);
                Log.w("로그인 - 비밀번호 입력", password);

                call.enqueue(new Callback<UserInfo>() {
                    @Override
                    public void onResponse(Call<UserInfo> call, @NonNull Response<UserInfo> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.w("로그인", "성공");
                            UserInfo userInfo = response.body();
                            sharedPreferencesHelper.saveUserInfo(userInfo);

                            UserInfo user = sharedPreferencesHelper.getUserInfo();
                            Log.e("유저 정보", user.getUserName());

                            Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_LONG).show();

                            loadUserGroups();

                            Intent intent = new Intent(MainActivity.this, your_new_home.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInfo> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "네트워크 오류", Toast.LENGTH_LONG).show();
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

    //그룹 정보 불러오기
    private void loadUserGroups() {
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        UserInfo user = sharedPreferencesHelper.getUserInfo();

        sharedPreferencesGroup = new SharedPreferencesGroup(this);
        String userGroupKey = user.getGroupKey();
        if(userGroupKey == null) {
            Toast.makeText(MainActivity.this, "아직 그룹이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitClient.getInstance();
        UserRetrofitInterface loadUserGroups = RetrofitClient.getUserRetrofitInterface();

        Call<Group> call = loadUserGroups.getUserGroups(user);
        call.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(@NonNull Call<Group> call, @NonNull Response<Group> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Group group = response.body();
                    Log.w("그룹 마스터", group.getGroupMaster());
                    String groupName = group.getGroupName();
                    if (groupName != null) { // null 체크 추가
                        sharedPreferencesGroup.saveGroupInfo(group);

                        Toast.makeText(MainActivity.this, "그룹원의 위치를 표시합니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "그룹 이름이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "아직 그룹이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Group> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "네트워크 문제로 그룹 정보를 불러오는데 실패했습니다: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}