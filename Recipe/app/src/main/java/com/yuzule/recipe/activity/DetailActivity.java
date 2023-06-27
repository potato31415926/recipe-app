package com.yuzule.recipe.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson2.JSONObject;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.yuzule.recipe.R;
import com.yuzule.recipe.RecipeApplication;
import com.yuzule.recipe.adapter.MaterialAdapter;
import com.yuzule.recipe.adapter.ProcessAdapter;
import com.yuzule.recipe.adapter.TagAdapter;
import com.yuzule.recipe.model.Collections;
import com.yuzule.recipe.model.Material;
import com.yuzule.recipe.model.Process;
import com.yuzule.recipe.model.Recipe;
import com.yuzule.recipe.model.User;
import com.yuzule.recipe.service.CollectService;
import com.yuzule.recipe.service.UnCollectService;
import com.yuzule.recipe.view.NestedRecyclerView;

import es.dmoral.toasty.Toasty;

public class DetailActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    NestedRecyclerView tag, material, process;
    Recipe recipe;
    TextView content, people, prepare, cook, star;
    ImageView imageView;
    User user;
    RecipeApplication application;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.yzl.broadcast.collect".equals(intent.getAction())) {
                String data = intent.getStringExtra("message");
                Toasty.info(getApplicationContext(), data, Toasty.LENGTH_SHORT).show();
                application.insertCollections(recipe);
            } else if ("com.yzl.broadcast.uncollect".equals(intent.getAction())) {
                String data = intent.getStringExtra("message");
                Toasty.info(getApplicationContext(), data, Toasty.LENGTH_SHORT).show();
                application.deleteCollections(recipe);
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
        setContentView(R.layout.activity_detail);
        toolbar = findViewById(R.id.toolbar);
        tag = findViewById(R.id.tag);
        material = findViewById(R.id.material);
        process = findViewById(R.id.process);
        content = findViewById(R.id.content);
        people = findViewById(R.id.people);
        star = findViewById(R.id.star);
        prepare = findViewById(R.id.prepare);
        cook = findViewById(R.id.cook);
        imageView = findViewById(R.id.image);
        recipe = JSONObject.parseObject(getIntent().getStringExtra("content"), Recipe.class);
        init();
        // 注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.yzl.broadcast.collect");
        filter.addAction("com.yzl.broadcast.uncollect");
        registerReceiver(receiver, filter);

        application = (RecipeApplication) getApplicationContext();
        user = application.getUser();
        Collections collections = application.getCollections();


        star.setOnClickListener(new View.OnClickListener() {
            int i = 0;

            // 让该菜谱与收藏的内容一一比较，查看是否已被收藏
            {
                for (int j = 0; j < collections.getContents().size(); j++) {
                    if (collections.getContents().get(j).getId() == recipe.getId()) {
                        i = 1;
                        star.setSelected(true);
                    }

                }
            }

            @Override
            public void onClick(View v) {
                v.setSelected(i == 0);
                //最开始未选择时为false,i==0返回true,设置为state_selected="true"的图片
                if (i == 0) {
                    i = 1;
                    Intent intent = new Intent(DetailActivity.this, CollectService.class);
                    intent.putExtra("user_id", user.getUser_id());
                    intent.putExtra("content_id", recipe.getId());
                    intent.putExtra("content", JSONObject.toJSONString(recipe));
                    startService(intent);
                } else {
                    i = 0;
                    Intent intent = new Intent(DetailActivity.this, UnCollectService.class);
                    intent.putExtra("user_id", user.getUser_id());
                    intent.putExtra("content_id", recipe.getId());
                    startService(intent);
                }
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 渲染数据到详情页内容
    private void init() {
        toolbar.setTitle(recipe.getName());
        Glide.with(this)
                .load(recipe.getPic())
                .into(imageView);
        tag.setLayoutManager(new FlexboxLayoutManager(getApplicationContext(), FlexDirection.ROW, FlexWrap.WRAP));
        tag.setAdapter(new TagAdapter(recipe.getTag().split(",")));
        material.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        material.setAdapter(new MaterialAdapter(recipe.getMaterial().toArray(new Material[recipe.getMaterial().size()])));
        process.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        process.setAdapter(new ProcessAdapter(recipe.getProcess().toArray((new Process[recipe.getProcess().size()]))));
        people.setText(recipe.getPeoplenum());
        prepare.setText(recipe.getPreparetime());
        cook.setText(recipe.getCookingtime());
        content.setText(Html.fromHtml(recipe.getContent(), Html.FROM_HTML_MODE_LEGACY));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消注册广播接收器
        unregisterReceiver(receiver);
    }

}