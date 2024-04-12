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
import com.example.guardiango.server.RetrofitClient;
import com.example.guardiango.server.UserRetrofitInterface;
import com.example.guardiango.entity.Groupname;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class home_group extends AppCompatActivity {
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

        //그룹생성 버튼 클릭
        Button addButton = findViewById(R.id.home_group_create);
        addButton.setOnClickListener(v -> showCreateGroupDialog());

        //그룹참가 버튼 클릭
        Button joinButton = findViewById(R.id.home_group_join);
        joinButton.setOnClickListener(v -> groupJoin());

        //리스트 클릭
        groupListView.setOnItemClickListener((parent, view, position, id) -> showGroupDetailDialog(groupList.get(position)));


    }

    //그룹생성 버튼 이벤트 처리
    private void showCreateGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("그룹 생성");

        // 사용자 이름을 입력 받습니다.
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("사용자 이름을 입력하세요");
        builder.setView(input);

        builder.setPositiveButton("확인", (dialog, which) -> {
            String userName = input.getText().toString();
            if (!userName.isEmpty()) {
                // 서버로 입력한 코드 보내기
                RetrofitClient retrofitClient = RetrofitClient.getInstance();
                UserRetrofitInterface Interface = RetrofitClient.getUserRetrofitInterface();

                Groupname groupName = new Groupname(userName);
                Gson gson = new Gson();
                String json = gson.toJson(groupName);
                Log.d("Group Create", "Sending JSON: " + json);

                Call<ResponseBody> call = Interface.groupCreate(groupName);
                call.clone().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Log.w("그룹생성","성공");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("그룹생성","실패");
                    }
                });

                // '님의 그룹'을 붙여서 리스트에 추가합니다.
                groupList.add(userName + "님의 그룹");
                adapter.notifyDataSetChanged();
                Toast.makeText(home_group.this, userName + "님의 그룹이 생성되었습니다.", Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    //그룹참가 이벤트
    private void groupJoin() {
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
