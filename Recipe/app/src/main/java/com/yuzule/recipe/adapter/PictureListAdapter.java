package com.yuzule.recipe.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson2.JSONObject;
import com.bumptech.glide.Glide;
import com.yuzule.recipe.R;
import com.yuzule.recipe.model.User;
import com.yuzule.recipe.service.ModifyService;
import com.yuzule.recipe.view.NestedRecyclerView;

public class PictureListAdapter extends NestedRecyclerView.Adapter<PictureListAdapter.ViewHolder> {

    private final String[] Data;
    private final User user;

    public PictureListAdapter(User user, String[] data) {
        Data = data;
        this.user = user;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(holder.itemView.getContext())
                .load(Data[position])
                .into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ModifyService.class);
                user.setUrl(Data[position]);
                intent.putExtra("user", JSONObject.toJSONString(user));
                v.getContext().startService(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Data.length;
    }

    static class ViewHolder extends NestedRecyclerView.ViewHolder {

        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}