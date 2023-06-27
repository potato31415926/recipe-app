package com.yuzule.recipe.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson2.JSONObject;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.material.appbar.MaterialToolbar;
import com.yuzule.recipe.DBHelper;
import com.yuzule.recipe.R;
import com.yuzule.recipe.adapter.SearchListAdapter;
import com.yuzule.recipe.model.Result;
import com.yuzule.recipe.model.Suggestion;
import com.yuzule.recipe.service.RecipeService;
import com.yuzule.recipe.view.NestedRecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SearchListActivity extends AppCompatActivity {
    String keyword;
    Result result;
    FloatingSearchView floatingSearchView;
    NestedRecyclerView recipe_list;
    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (message.what == 1) {
                String data = (String) message.obj;
                result = JSONObject.parseObject(data, Result.class);
                recipe_list.setAdapter(new SearchListAdapter(result.getList()));
                recipe_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recipe_list.setNestedScrollingEnabled(false);
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
    MaterialToolbar toolbar;
    SQLiteDatabase db;
    Handler handler;
    List<Suggestion> suggestionList;

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
        setContentView(R.layout.activity_searchlist);
        recipe_list = findViewById(R.id.recipe_list);
        floatingSearchView = findViewById(R.id.searchView);
        toolbar = findViewById(R.id.toolbar);
        keyword = getIntent().getStringExtra("keyword");
        Intent intent = new Intent(SearchListActivity.this, RecipeService.class);
        intent.putExtra("keyword", keyword);
        startService(intent);

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        // 添加查询更改监听器
        floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                // 过滤不匹配的项目
                querySuggestions(newQuery);
            }
        });

        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSearchAction(String currentQuery) {
                if (currentQuery.equals("")) {
                    Toasty.warning(getApplicationContext(), "搜索内容不能为空", Toasty.LENGTH_SHORT).show();
                } else {
                    // 在这里执行搜索操作
                    db.execSQL("INSERT OR IGNORE INTO suggestions VALUES('" + currentQuery + "')");
                    Toasty.success(getApplicationContext(), "搜索成功", Toasty.LENGTH_SHORT).show();
                    intent.putExtra("keyword", currentQuery);
                    startService(intent);
                }
            }

            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                // 获取所选搜索建议的文本并将其设置为搜索框的文本
                String suggestion = searchSuggestion.getBody();
                floatingSearchView.setSearchText(suggestion);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void querySuggestions(String query) {
        if (TextUtils.isEmpty(query)) {
            return;
        }
        new Thread(() -> {
            suggestionList = new ArrayList<>();
            Cursor c = db.rawQuery("SELECT DISTINCT * FROM suggestions WHERE title LIKE '%" + query + "%'", null);
            while (c.moveToNext()) {
                suggestionList.add(new Suggestion(c.getString(0)));
            }
            c.close();
            runOnUiThread(() -> floatingSearchView.swapSuggestions(suggestionList));
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.yzl.broadcast.recipes");
        registerReceiver(myReceiver, filter);
    }


    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

}
