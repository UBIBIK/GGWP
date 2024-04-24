package com.example.guardiango.screen;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guardiango.Custom.LocationService;
import com.example.guardiango.Custom.MemberAdapter;
import com.example.guardiango.R;
import com.example.guardiango.entity.Group;
import com.example.guardiango.entity.LocationData;
import com.example.guardiango.entity.SharedPreferencesGroup;
import com.example.guardiango.entity.UserInfo;
import com.example.guardiango.server.RetrofitClient;
import com.example.guardiango.entity.SharedPreferencesHelper;
import com.example.guardiango.server.UserRetrofitInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TODO : 방장이 아닌 멤버 그룹 나가기 기능 추가하기
public class home_group extends AppCompatActivity {
    private SharedPreferencesHelper sharedPreferencesHelper;
    private SharedPreferencesGroup sharedPreferencesGroup;
    private LocationService locationService;

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

        //위치 서비스 등록
        locationService = new LocationService(this);

        //소속된 그룹이 있는지 확인
        loadUserGroups();

        //그룹생성 버튼 클릭
        Button addButton = findViewById(R.id.home_group_create);
        addButton.setOnClickListener(v -> showCreateGroupDialog());

        //그룹참가 버튼 클릭
        Button joinButton = findViewById(R.id.home_group_join);
        joinButton.setOnClickListener(v -> groupJoin());

        //리스트 클릭
        groupListView.setOnItemClickListener((parent, view, position, id) -> showGroupDetailDialog(sharedPreferencesGroup.getGroupInfo()));

    }

    //리스트 불러오기
    private void loadUserGroups() {
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        UserInfo user = sharedPreferencesHelper.getUserInfo();

        sharedPreferencesGroup = new SharedPreferencesGroup(this);
        String userGroupKey = user.getGroupKey();
        if(userGroupKey == null) {
            Toast.makeText(home_group.this, "그룹이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

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
                        groupList.clear();
                        groupList.add(groupName);
                        adapter.notifyDataSetChanged();
                        sharedPreferencesGroup.saveGroupInfo(group);

                        Toast.makeText(home_group.this, "그룹이 존재합니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(home_group.this, "그룹 이름이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(home_group.this, "그룹이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Group> call, @NonNull Throwable t) {
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

        // SharedPreferences에서 그룹 정보 가져오기
        sharedPreferencesGroup = new SharedPreferencesGroup(this);

        //로그인 정보에 위치 정보 저장
        LocationData currentLocation = locationService.getCurrentLocationData();
        user.getLocationInfo().put("latitude", currentLocation.getLatitude());
        user.getLocationInfo().put("longitude", currentLocation.getLongitude());

        Gson gson = new Gson();
        String json = gson.toJson(user);
        Log.d("Group Create", "Sending JSON: " + json);

        // 서버에 그룹 생성 요청
        UserRetrofitInterface Interface = RetrofitClient.getUserRetrofitInterface();

        Call<Group> call = Interface.groupCreate(user);
        call.clone().enqueue(new Callback<Group>() {
            @Override
            public void onResponse(@NonNull Call<Group> call, @NonNull Response<Group> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    user.setGroupKey(response.body().getGroupKey());
                    if (user.getGroupKey() != null) {
                        sharedPreferencesHelper.saveUserInfo(user);

                        // 리스트에 추가
                        String userName = user.getUserName();
                        groupList.add(userName + "님의 그룹");
                        adapter.notifyDataSetChanged();
                        sharedPreferencesGroup.saveGroupInfo(response.body());
                        Toast.makeText(home_group.this, userName + "님의 그룹이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("그룹생성", "응답은 성공이나 유저 정보가 누락됨");
                        Toast.makeText(home_group.this, "유저 정보를 받지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Group> call, @NonNull Throwable t) {
                Log.e("그룹생성", "네트워크 실패", t);
                Toast.makeText(home_group.this, "네트워크 오류로 그룹 생성 실패: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



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
            String groupKey = input.getText().toString();
            if (!groupKey.isEmpty()) {
                // SharedPreferences에서 사용자 정보 가져오기
                sharedPreferencesHelper = new SharedPreferencesHelper(this);
                UserInfo user = sharedPreferencesHelper.getUserInfo();
                user.setGroupKey(groupKey);

                // SharedPreferences에서 그룹 정보 가져오기
                sharedPreferencesGroup = new SharedPreferencesGroup(this);

                //로그인 정보에 위치 정보 저장
                LocationData currentLocation = locationService.getCurrentLocationData();
                user.getLocationInfo().put("latitude", currentLocation.getLatitude());
                user.getLocationInfo().put("longitude", currentLocation.getLongitude());

                UserRetrofitInterface Interface = RetrofitClient.getUserRetrofitInterface();

                Gson gson = new Gson();
                String json = gson.toJson(user);
                Log.d("Group join", "Sending code: " + json);

                Call<Group> call = Interface.groupJoin(user);
                call.clone().enqueue(new Callback<Group>() {
                    @Override
                    public void onResponse(@NonNull Call<Group> call, @NonNull Response<Group> response) {
                        if(response.isSuccessful()) {
                            //로그인 정보에 키값 삽입
                            assert response.body() != null;
                            user.setGroupKey(response.body().getGroupKey());
                            sharedPreferencesHelper.saveUserInfo(user);

                            //그룹 정보 변경
                            sharedPreferencesGroup.saveGroupInfo(response.body());

                            // 리스트에 추가
                            String groupName = response.body().getGroupName();
                            groupList.add(groupName);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(home_group.this, "그룹에 참가되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Group> call, @NonNull Throwable t) {
                        Toast.makeText(home_group.this, "그룹 참가 요청 실패",Toast.LENGTH_LONG).show();
                        Log.e("그룹참가", "실패", t);
                    }
                });
            }
        });

        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // 리스트 클릭 이벤트
    private void showGroupDetailDialog(Group group) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(group.getGroupName() + " - 그룹 상세 정보");

        ListView memberListView = new ListView(this);
        MemberAdapter memberAdapter = new MemberAdapter(this, group.getGroupMember(),
                group.getGroupMaster().equals(sharedPreferencesHelper.getUserInfo().getUserEmail()));
        memberListView.setAdapter(memberAdapter);
        builder.setView(memberListView);

        // 닫기 버튼 항상 추가
        builder.setNeutralButton("닫기", (dialog, which) -> dialog.dismiss());

        // 삭제 버튼 추가 (권한이 있을 경우)
        if(sharedPreferencesHelper.getUserInfo().getUserEmail()
                .equals(group.getGroupMaster())) {
            builder.setPositiveButton("삭제", (dialog, which) -> deleteGroup());

            // 초대코드 복사 버튼 추가
            builder.setNegativeButton("초대코드 복사", (dialog, which) -> {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("groupKey", group.getGroupKey());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(home_group.this, "초대코드가 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show();
            });
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    //그룹 삭제
    private void deleteGroup() {
        // SharedPreferences에서 사용자 정보 가져오기
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        UserInfo user = sharedPreferencesHelper.getUserInfo();

        // SharedPreferences에서 그룹 정보 가져오기
        sharedPreferencesGroup = new SharedPreferencesGroup(this);
        Group group = sharedPreferencesGroup.getGroupInfo();

        UserRetrofitInterface interfaceAPI = RetrofitClient.getUserRetrofitInterface();

        // 서버에 그룹 삭제 요청
        Call<ResponseBody> call = interfaceAPI.groupDelete(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // 그룹 삭제 후 UI 업데이트
                    groupList.remove(0);
                    adapter.notifyDataSetChanged();

                    // 사용자 정보 업데이트
                    user.setGroupKey(null);
                    sharedPreferencesHelper.saveUserInfo(user);

                    // 그룹 정보 지우기
                    sharedPreferencesGroup.clearGroupInfo();

                    Toast.makeText(home_group.this, "그룹이 성공적으로 삭제되었습니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(home_group.this, "그룹 삭제 요청 실패",Toast.LENGTH_LONG).show();
                Log.e("그룹삭제", "실패", t);
            }
        });
    }

    //메뉴 항목 구현
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_group_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_refresh) {
            // 새로고침 로직
            loadUserGroups();
            sendLocationToServer();
            //TODO : 본인의 위치를 서버로 전송
            return true;
        } else if (id == R.id.menu_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //로그아웃
    private void logout() {
        // 모든 사용자 정보와 그룹 정보 삭제
        if (sharedPreferencesHelper != null) {
            sharedPreferencesHelper.clearUserInfo();
        }
        if (sharedPreferencesGroup != null) {
            sharedPreferencesGroup.clearGroupInfo();
        }

        // 로그아웃 후 로그인 화면으로 이동 또는 앱 재시작
        Intent restartIntent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        assert restartIntent != null;
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(restartIntent);
    }

    //위치 전송
    private void sendLocationToServer() {
        //그룹 정보 가져오기
        sharedPreferencesGroup = new SharedPreferencesGroup(this);

        //사용자 정보 가져와서 위치 정보 삽입
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        UserInfo user = sharedPreferencesHelper.getUserInfo();
        LocationData currentLocation = locationService.getCurrentLocationData();

        user.getLocationInfo().put("latitude", currentLocation.getLatitude());
        user.getLocationInfo().put("longitude", currentLocation.getLongitude());

        if (user != null) {
            // 서버에 위치 정보 전송
            UserRetrofitInterface apiService = RetrofitClient.getUserRetrofitInterface();
            Call<Group> call = apiService.updateLocation(user);
            call.enqueue(new Callback<Group>() {
                @Override
                public void onResponse(@NonNull Call<Group> call, @NonNull Response<Group> response) {
                    if (response.isSuccessful()) {
                        sharedPreferencesHelper.saveUserInfo(user);
                        sharedPreferencesGroup.saveGroupInfo(response.body());

                        Toast.makeText(getApplicationContext(), "위치 업데이트 성공", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "위치 업데이트 실패", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Group> call, @NonNull Throwable t) {
                    Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "위치 정보 없음", Toast.LENGTH_SHORT).show();
        }
    }

}
