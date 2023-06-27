package com.yuzule.recipe.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yuzule.recipe.R;
import com.yuzule.recipe.view.NestedRecyclerView;

public class TagAdapter extends NestedRecyclerView.Adapter<TagAdapter.ViewHolder> {

    private final String[] Data;

    public TagAdapter(String[] data) {
        Data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.text.setText(Data[position]);
    }

    @Override
    public int getItemCount() {
        return Data.length;
    }

    static class ViewHolder extends NestedRecyclerView.ViewHolder {

        TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }
}