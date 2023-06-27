package com.yuzule.recipe.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.yuzule.recipe.R;
import com.yuzule.recipe.RecipeApplication;
import com.yuzule.recipe.adapter.SearchListAdapter;
import com.yuzule.recipe.model.Collections;
import com.yuzule.recipe.view.NestedRecyclerView;


public class CollectFragment extends Fragment {

    MaterialToolbar toolbar;
    NestedRecyclerView recipe_list;
    Collections collections;
    RecipeApplication application;
    LinearLayout ifEmpty;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.collect_fragment, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        recipe_list = view.findViewById(R.id.recipe_list);
        ifEmpty = view.findViewById(R.id.ifEmpty);
        application = (RecipeApplication) getActivity().getApplicationContext();
        collections = application.getCollections();
        recipe_list.setNestedScrollingEnabled(false);
        recipe_list.setLayoutManager(new LinearLayoutManager(getContext()));
        recipe_list.setAdapter(new SearchListAdapter(collections.getContents()));

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        recipe_list.getAdapter().notifyDataSetChanged();
        if (collections.getContents().size() == 0)
            ifEmpty.setVisibility(View.VISIBLE);
        else
            ifEmpty.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
