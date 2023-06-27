package com.yuzule.recipe.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yuzule.recipe.R;
import com.yuzule.recipe.model.Material;
import com.yuzule.recipe.view.NestedRecyclerView;

public class MaterialAdapter extends NestedRecyclerView.Adapter<MaterialAdapter.ViewHolder> {

    private final Material[] Data;

    public MaterialAdapter(Material[] data) {
        Data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.material_name.setText(Data[position].getMname());
        holder.material_count.setText(Data[position].getAmount());
    }

    @Override
    public int getItemCount() {
        return Data.length;
    }

    static class ViewHolder extends NestedRecyclerView.ViewHolder {

        TextView material_name, material_count;

        ViewHolder(View itemView) {
            super(itemView);
            material_name = itemView.findViewById(R.id.material_name);
            material_count = itemView.findViewById(R.id.material_count);
        }
    }
}