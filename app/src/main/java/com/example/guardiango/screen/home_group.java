package com.example.guardiango.screen;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guardiango.R;
import com.example.guardiango.entity.Groupcode;
import com.example.guardiango.entity.Groupcreate;
import com.example.guardiango.entity.UserInfo;
import com.example.guardiango.server.RetrofitClient;
import com.example.guardiango.server.SharedPreferencesHelper;
import com.example.guardiango.server.UserRetrofitInterface;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class home_group extends AppCompatActivity {
    private SharedPreferencesHelper sharedPreferencesHelper;

    ListView groupListView;
    ArrayList<String> groupList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_group);

        groupListView = findViewById(R.id.groupListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, groupList);
        groupListView.setAdapter(adapter);

        //소속된 그룹이 있는지 확인
        loadUserGroups();

        //그룹생성 버튼 클릭
        Button addButton = findViewById(R.id.home_group_create);
        addButton.setOnClickListener(v -> showCreateGroupDialog());

        //그룹참가 버튼 클릭
        Button joinButton = findViewById(R.id.home_group_join);
        joinButton.setOnClickListener(v -> groupJoin());

        //리스트 클릭
        groupListView.setOnItemClickListener((parent, view, position, id) -> showGroupDetailDialog(groupList.get(position)));

    }

    //리스트 불러오기
    private void loadUserGroups() {
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        UserInfo user = sharedPreferencesHelper.getUserInfo();
        String userKey = user.getGroupKey();
        if(userKey == null) {return;}

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        UserRetrofitInterface loadUserGroups = retrofitClient.getUserRetrofitInterface();

        Call<ResponseBody> call = loadUserGroups.getUserGroups(userKey);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String groups = null;
                    try {
                        groups = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    groupList.clear();
                    groupList.add(groups);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(home_group.this, "그룹이 존재합니다..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(home_group.this, "그룹이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(home_group.this, "네트워크 문제로 그룹 정보를 불러오는데 실패했습니다: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // 그룹생성 버튼 이벤트 처리
    private void showCreateGroupDialog() {
        // 이미 생성된 그룹이 있는지 확인
        if (!groupList.isEmpty()) {
            Toast.makeText(home_group.this, "이미 생성된 그룹이 있습니다. 추가 그룹 생성이 제한됩니다.", Toast.LENGTH_LONG).show();
            return; // 이 경우 더 이상 진행하지 않고 함수를 종료
        }

        // SharedPreferences에서 사용자 정보 가져오기
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        UserInfo user = sharedPreferencesHelper.getUserInfo();
        Groupcreate groupcreate = new Groupcreate(user.getUserId());

        Gson gson = new Gson();
        String json = gson.toJson(groupcreate);
        Log.d("Group Create", "Sending JSON: " + json);

        // 서버에 그룹 생성 요청
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        UserRetrofitInterface Interface = retrofitClient.getUserRetrofitInterface();

        Call<ResponseBody> call = Interface.groupCreate(groupcreate);
        call.clone().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String groupKey = null;
                    try {
                        groupKey = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.w("그룹생성", "성공, 그룹 키: " + groupKey);

                    // 유저 로그인 정보에 그룹 키 값도 저장
                    user.setGroupKey(groupKey);
                    sharedPreferencesHelper.saveUserInfo(user);

                    // 리스트에 추가
                    String userName = user.getUserName();
                    groupList.add(userName + "님의 그룹");
                    adapter.notifyDataSetChanged();
                    Toast.makeText(home_group.this, userName + "님의 그룹이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("그룹생성", "네트워크 실패", t);
                Toast.makeText(home_group.this, "네트워크 오류로 그룹 생성 실패: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    //그룹참가 이벤트
    private void groupJoin() {
        // 이미 생성된 그룹이 있는지 확인
        if (!groupList.isEmpty()) {
            Toast.makeText(home_group.this, "이미 생성된 그룹이 있습니다. 추가 그룹 생성이 제한됩니다.", Toast.LENGTH_LONG).show();
            return; // 이 경우 더 이상 진행하지 않고 함수를 종료
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("그룹 참가");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("그룹 코드를 입력하세요");
        builder.setView(input);

        builder.setPositiveButton("확인", (dialog, which) -> {
            String groupCode = input.getText().toString();
            if (!groupCode.isEmpty()) {
                RetrofitClient retrofitClient = RetrofitClient.getInstance();
                UserRetrofitInterface Interface = retrofitClient.getUserRetrofitInterface();

                Groupcode Code = new Groupcode(groupCode);
                Gson gson = new Gson();
                String json = gson.toJson(Code);
                Log.d("Group join", "Sending code: " + json);

                Call<ResponseBody> call = Interface.groupjoin(Code);
                call.clone().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String message = response.body().string();
                            Toast.makeText(home_group.this, message, Toast.LENGTH_LONG).show();
                            Log.w("그룹참가", message);
                        } catch (Exception e) {
                            Log.e("그룹참가", "응답 파싱 실패", e);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(home_group.this, "그룹 참가 요청 실패: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("그룹참가", "실패", t);
                    }
                });
            }
        });

        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    //TODO:리스트 클릭 이벤트 데이터베이스에서 불러오기 필요함
    private void showGroupDetailDialog(String groupName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("그룹 상세 정보");
        builder.setMessage("그룹 코드: 1111\n그룹원: 최우빈");

        builder.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());
        builder.setNegativeButton("삭제", (dialog, which) -> {
            groupList.remove(groupName);
            adapter.notifyDataSetChanged();
            Toast.makeText(home_group.this, groupName + "이(가) 삭제되었습니다.", Toast.LENGTH_SHORT).show();
        });

        builder.show();
    }
}
