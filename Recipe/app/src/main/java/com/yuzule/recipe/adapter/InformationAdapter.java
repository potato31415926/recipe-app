package com.yuzule.recipe.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yuzule.recipe.R;
import com.yuzule.recipe.activity.ModifyPasswordActivity;
import com.yuzule.recipe.activity.ModifyPhoneActivity;
import com.yuzule.recipe.view.NestedRecyclerView;

public class InformationAdapter extends NestedRecyclerView.Adapter<InformationAdapter.ViewHolder> {

    private final String[] titles = {"密码", "电话"};
    private final String[] Data;

    public InformationAdapter(String[] data) {
        Data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_information_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InformationAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(titles[position]);
        holder.title_value.setText(Data[position]);
        holder.information_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    Intent intent = new Intent(v.getContext(), ModifyPasswordActivity.class);
                    v.getContext().startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(v.getContext(), ModifyPhoneActivity.class);
                    v.getContext().startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return Data.length;
    }

    static class ViewHolder extends NestedRecyclerView.ViewHolder {

        TextView title, title_value;
        LinearLayout information_list;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            title_value = itemView.findViewById(R.id.title_value);
            information_list = itemView.findViewById(R.id.information_list);
        }
    }
}