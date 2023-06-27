package com.yuzule.recipe.adapter;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.yuzule.recipe.R;
import com.yuzule.recipe.model.Process;
import com.yuzule.recipe.view.NestedRecyclerView;

public class ProcessAdapter extends NestedRecyclerView.Adapter<ProcessAdapter.ViewHolder> {

    private static int i = 1;
    private final Process[] Data;

    public ProcessAdapter(Process[] data) {
        Data = data;
        i = 1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.process_list_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProcessAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.process_content.setText((i++) + ". " + Html.fromHtml(Data[position].getPcontent(), Html.FROM_HTML_MODE_LEGACY));
        Glide.with(holder.itemView.getContext())
                .load(Data[position].getPic())
                .into(holder.process_image);
    }

    @Override
    public int getItemCount() {
        return Data.length;
    }

    static class ViewHolder extends NestedRecyclerView.ViewHolder {

        TextView process_content;
        ImageView process_image;

        ViewHolder(View itemView) {
            super(itemView);
            process_content = itemView.findViewById(R.id.process_content);
            process_image = itemView.findViewById(R.id.process_image);
        }
    }
}