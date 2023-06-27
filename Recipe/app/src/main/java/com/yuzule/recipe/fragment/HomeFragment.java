package com.yuzule.recipe.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.fastjson2.JSONObject;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yuzule.recipe.R;
import com.yuzule.recipe.activity.SearchActivity;
import com.yuzule.recipe.activity.SearchListActivity;
import com.yuzule.recipe.adapter.MenuListAdapter;
import com.yuzule.recipe.model.Result;
import com.yuzule.recipe.service.RecipeService;
import com.yuzule.recipe.view.NestedRecyclerView;

public class HomeFragment extends Fragment {

    Result result;
    FloatingActionButton toTop;
    MaterialButton toSearch;
    NestedRecyclerView recyclerView;
    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (message.what == 1) {
                String data = (String) message.obj;
                result = JSONObject.parseObject(data, Result.class);
                recyclerView.setAdapter(new MenuListAdapter(result));
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                recyclerView.setNestedScrollingEnabled(false);
            }
            return false;
        }
    });
    BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.yzl.broadcast.recipes".equals(intent.getAction())) {
                String data = intent.getStringExtra("result");
                Message message = mHandler.obtainMessage(1, data);
                mHandler.sendMessage(message);
            }
        }
    };
    MaterialButton cold_food, rice_food, chip, cake, vegetable, pork;
    ScrollView scrollView;
    LinearLayout topPanel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        toTop = view.findViewById(R.id.toTop);
        toSearch = view.findViewById(R.id.toSearch);
        scrollView = view.findViewById(R.id.scrollView);
        cold_food = view.findViewById(R.id.cold_food);
        rice_food = view.findViewById(R.id.rice_food);
        chip = view.findViewById(R.id.chip);
        cake = view.findViewById(R.id.cake);
        vegetable = view.findViewById(R.id.vegetable);
        pork = view.findViewById(R.id.pork);
        topPanel = view.findViewById(R.id.topPanel);

        SharedPreferences prefs = getActivity().getSharedPreferences("Recipe", MODE_PRIVATE);
        int theme = prefs.getInt("theme", 0); // 0为亮色主题，1为暗色主题
        if (theme == 0) {
            topPanel.setBackgroundResource(R.drawable.orange);
        } else {
            topPanel.setBackgroundResource(R.drawable.green);
        }

        // 启动获取菜单的服务
        Intent intent = new Intent(getContext(), RecipeService.class);
        intent.putExtra("keyword", "牛肉");
        getContext().startService(intent);

        toTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.smoothScrollTo(0, 0); // 滚动到顶部
            }
        });

        toSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        cold_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), SearchListActivity.class);
                intent1.putExtra("keyword", "凉菜");
                startActivity(intent1);
            }
        });

        rice_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), SearchListActivity.class);
                intent1.putExtra("keyword", "下饭菜");
                startActivity(intent1);
            }
        });

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), SearchListActivity.class);
                intent1.putExtra("keyword", "小吃");
                startActivity(intent1);
            }
        });

        cake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), SearchListActivity.class);
                intent1.putExtra("keyword", "蛋糕");
                startActivity(intent1);
            }
        });

        vegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), SearchListActivity.class);
                intent1.putExtra("keyword", "素菜");
                startActivity(intent1);
            }
        });

        pork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), SearchListActivity.class);
                intent1.putExtra("keyword", "猪肉");
                startActivity(intent1);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.yzl.broadcast.recipes");
        getActivity().registerReceiver(myReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(myReceiver);
    }
}