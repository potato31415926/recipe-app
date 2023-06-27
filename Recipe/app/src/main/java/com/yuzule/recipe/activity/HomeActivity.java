package com.yuzule.recipe.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson2.JSONObject;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.yuzule.recipe.R;
import com.yuzule.recipe.RecipeApplication;
import com.yuzule.recipe.fragment.CollectFragment;
import com.yuzule.recipe.fragment.HomeFragment;
import com.yuzule.recipe.fragment.MineFragment;
import com.yuzule.recipe.model.Collections;
import com.yuzule.recipe.model.User;
import com.yuzule.recipe.service.CollectionsService;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecipeApplication application;
    private User user;
    private Collections collections;
    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.yzl.broadcast.collections".equals(intent.getAction())) {
                String data = intent.getStringExtra("collections");
                collections = JSONObject.parseObject(data, Collections.class);
                application.setCollections(collections);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("Recipe", MODE_PRIVATE);
        int theme = prefs.getInt("theme", 0); // 0为亮色主题，1为暗色主题
        if (theme == 0) {
            setTheme(R.style.Theme_Recipe);
        } else {
            setTheme(R.style.Theme_Recipe_Dark);
        }
        setContentView(R.layout.activity_home);

        application = (RecipeApplication) getApplicationContext();

        user = application.getUser();

        bottomNavigationView = findViewById(R.id.bottomNav);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.yzl.broadcast.collections");
        registerReceiver(myReceiver, filter);

        Intent intent = new Intent(HomeActivity.this, CollectionsService.class);
        startService(intent);

        // 第一次创建或重新创建时，加载首页到容器中
        if (savedInstanceState == null) {
            if (getIntent().getStringExtra("mine") != null) {
                bottomNavigationView.setSelectedItemId(R.id.mine);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new MineFragment()).commit();
            } else
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.collect:
                        fragment = new CollectFragment();
                        break;
                    case R.id.mine:
                        fragment = new MineFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                return true;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }
}