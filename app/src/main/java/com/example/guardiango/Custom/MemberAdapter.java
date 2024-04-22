package com.example.guardiango.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guardiango.R;
import com.example.guardiango.entity.Group;
import com.example.guardiango.entity.SharedPreferencesGroup;
import com.example.guardiango.server.RetrofitClient;
import com.example.guardiango.server.UserRetrofitInterface;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberAdapter extends ArrayAdapter<Map<String, Object>> {
    private Context context;
    private List<Map<String, Object>> members;
    private boolean isGroupMaster;// Retrofit 인터페이스


    public MemberAdapter(Context context, List<Map<String, Object>> members, boolean isGroupMaster) {
        super(context, 0, members);
        this.context = context;
        this.members = members;
        this.isGroupMaster = isGroupMaster;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_member, parent, false);
        }

        SharedPreferencesGroup sharedPreferencesGroup = new SharedPreferencesGroup(this.getContext());

        TextView memberName = convertView.findViewById(R.id.member_name);
        TextView memberRole = convertView.findViewById(R.id.member_role);
        Button deleteButton = convertView.findViewById(R.id.delete_button);

        Map<String, Object> member = getItem(position);
        String memberNameText = (String) member.get("groupMemberName");
        memberName.setText(memberNameText);
        memberRole.setText((String) member.get("groupRole"));

        if (position != 0 && isGroupMaster) {
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(v -> {
                // 서버에 삭제 요청을 보냅니다.

                UserRetrofitInterface retrofitInterface = RetrofitClient.getUserRetrofitInterface();
                Call<Group> call = retrofitInterface.groupUserDelete(memberNameText);
                call.enqueue(new Callback<Group>() {
                    @Override
                    public void onResponse(Call<Group> call, Response<Group> response) {
                        if (response.isSuccessful()) {
                            members.remove(position);
                            notifyDataSetChanged();
                            Group updateGroup = response.body();
                            sharedPreferencesGroup.saveGroupInfo(updateGroup);
                            Toast.makeText(context, memberNameText + " 사용자가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "사용자 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Group> call, Throwable t) {
                        Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        } else {
            deleteButton.setVisibility(View.GONE);
        }

        return convertView;
    }
}
