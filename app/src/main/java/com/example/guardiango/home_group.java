package com.example.guardiango;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class home_group extends AppCompatActivity {
    String userid;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_group);

        Button addButton = findViewById(R.id.home_group_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInviteDialog();
            }
        });


    }

    public void showInviteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사용자 초대");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("전송", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendInvite(input.getText().toString());
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void sendInvite(String userId) {
        UserRetrofitInterface service = RetrofitClient.getUserRetrofitInterface();
        InviteRequest inviteRequest = new InviteRequest(userId);

        Call<Void> call = service.sendInvite(inviteRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 초대 요청 성공 시 처리
                    Toast.makeText(home_group.this, "초대 요청을 성공적으로 전송했습니다.", Toast.LENGTH_LONG).show();
                } else {
                    // 서버로부터 받은 에러 처리
                    Toast.makeText(home_group.this, "초대 요청 실패: " + response.errorBody().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // 네트워크 문제 등으로 인한 실패 처리
                Toast.makeText(home_group.this, "초대 요청 실패: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
