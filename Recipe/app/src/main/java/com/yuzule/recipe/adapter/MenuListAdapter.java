package com.yuzule.recipe.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.fastjson2.JSONObject;
import com.bumptech.glide.Glide;
import com.yuzule.recipe.R;
import com.yuzule.recipe.activity.DetailActivity;
import com.yuzule.recipe.model.Result;
import com.yuzule.recipe.view.NestedRecyclerView;

public class MenuListAdapter extends NestedRecyclerView.Adapter<MenuListAdapter.ViewHolder> {

    private final Result Data;

    public MenuListAdapter(Result data) {
        Data = data;
    }

    public String truncateString(String str) {
        if (str.length() > 12) {
            return str.substring(0, 12) + "...";
        } else {
            return str;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(holder.itemView.getContext())
                .load(Data.getList().get(position).getPic())
                .into(holder.image);
        holder.tags.setText(truncateString(Data.getList().get(position).getTag()));
        holder.name.setText((Data.getList().get(position).getName()));
        holder.menu_list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("content", JSONObject.toJSONString(Data.getList().get(position)));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Data.getList().size();
    }

    static class ViewHolder extends NestedRecyclerView.ViewHolder {

        ImageView image;
        TextView name, tags;
        ConstraintLayout menu_list_item;

        ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            tags = itemView.findViewById(R.id.tag);
            menu_list_item = itemView.findViewById(R.id.menu_list_item);
        }
    }
}