package com.example.guardiango.Custom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guardiango.R;

import java.util.List;

public class SwitchAdapter extends RecyclerView.Adapter<SwitchAdapter.ViewHolder> {

    private List<SwitchItem> switchItems;
    private OnSwitchCheckedChangeListener listener;

    public SwitchAdapter(List<SwitchItem> switchItems, OnSwitchCheckedChangeListener listener) {
        this.switchItems = switchItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_switch, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SwitchItem item = switchItems.get(position);
        holder.switchTitle.setText(item.getTitle());
        holder.switchCompat.setChecked(item.isChecked());
        holder.switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked);
            if (listener != null) {
                listener.onSwitchCheckedChanged(position, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return switchItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView switchTitle;
        public SwitchCompat switchCompat;

        public ViewHolder(View itemView) {
            super(itemView);
            switchTitle = itemView.findViewById(R.id.switchTitle);
            switchCompat = itemView.findViewById(R.id.switchCompat);
        }
    }

    public interface OnSwitchCheckedChangeListener {
        void onSwitchCheckedChanged(int position, boolean isChecked);
    }
}

