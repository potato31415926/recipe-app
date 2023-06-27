package com.yuzule.recipe.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.yuzule.recipe.R;
import com.yuzule.recipe.model.Suggestion;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final Suggestion[] Data;
    private final FloatingSearchView floatingSearchView;

    public HistoryAdapter(FloatingSearchView floatingSearchView, Suggestion[] data) {
        Data = data;
        this.floatingSearchView = floatingSearchView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.text.setText(Data[position].getBody());
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingSearchView.setSearchText(Data[position].getBody());
                floatingSearchView.setSearchFocused(true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Data.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }
}