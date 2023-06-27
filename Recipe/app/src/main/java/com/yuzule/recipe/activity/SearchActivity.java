package com.yuzule.recipe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.button.MaterialButton;
import com.yuzule.recipe.DBHelper;
import com.yuzule.recipe.R;
import com.yuzule.recipe.adapter.HistoryAdapter;
import com.yuzule.recipe.model.Suggestion;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SearchActivity extends AppCompatActivity {

    FloatingSearchView searchView;
    MaterialButton clear;
    SQLiteDatabase db;
    RecyclerView recyclerView;
    List<Suggestion> suggestionList;
    List<Suggestion> historyList;
    Handler handler;
    ImageView goBack;

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
        setContentView(R.layout.activity_search);
        searchView = findViewById(R.id.searchView);
        clear = findViewById(R.id.clear);
        recyclerView = findViewById(R.id.recyclerView);
        goBack = findViewById(R.id.goBack);

        handler = new Handler(Looper.getMainLooper());

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        getHistories();

        // 添加查询更改监听器
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                // 过滤不匹配的项目
                querySuggestions(newQuery);
            }
        });

        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSearchAction(String currentQuery) {
                if (currentQuery.equals("")) {
                    Toasty.warning(getApplicationContext(), "搜索内容不能为空", Toasty.LENGTH_SHORT).show();
                } else {
                    // 在这里执行搜索操作
                    db.execSQL("INSERT OR IGNORE INTO suggestions VALUES('" + currentQuery + "')");
                    getHistories();
                    Toasty.success(getApplicationContext(), "搜索成功", Toasty.LENGTH_SHORT).show();
                    Intent intent = new Intent(SearchActivity.this, SearchListActivity.class);
                    intent.putExtra("keyword", currentQuery);
                    startActivity(intent);
                }
            }

            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                // 获取所选搜索建议的文本并将其设置为搜索框的文本
                String suggestion = searchSuggestion.getBody();
                searchView.setSearchText(suggestion);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(SearchActivity.this)
                                .setTitle("删除确认")
                                .setMessage("您确定要删除搜索记录吗?")
                                .setNegativeButton(android.R.string.cancel, null)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.execSQL("DELETE FROM suggestions");
                                        getHistories();
                                    }
                                })

                                .show();

                    }
                });
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getHistories() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                historyList = new ArrayList<>();
                Cursor c = db.rawQuery("SELECT DISTINCT * FROM suggestions", null);
                while (c.moveToNext()) {
                    historyList.add(new Suggestion(c.getString(0)));
                }
                c.close();
                recyclerView.setLayoutManager(new FlexboxLayoutManager(getApplicationContext(), FlexDirection.ROW, FlexWrap.WRAP));
                recyclerView.setAdapter(new HistoryAdapter(searchView, historyList.toArray(new Suggestion[historyList.size()])));
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
            runOnUiThread(() -> searchView.swapSuggestions(suggestionList));
        }).start();
    }
}