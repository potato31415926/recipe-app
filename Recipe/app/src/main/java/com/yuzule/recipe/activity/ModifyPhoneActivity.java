package com.yuzule.recipe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson2.JSONObject;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.yuzule.recipe.R;
import com.yuzule.recipe.RecipeApplication;
import com.yuzule.recipe.model.User;
import com.yuzule.recipe.service.ModifyService;

public class ModifyPhoneActivity extends AppCompatActivity {


    MaterialToolbar toolbar;
    EditText phoneText;
    MaterialButton confirm;
    User user;

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
        setContentView(R.layout.activity_modify_phone);
        toolbar = findViewById(R.id.toolbar);
        phoneText = findViewById(R.id.phoneText);
        confirm = findViewById(R.id.confirm);
        RecipeApplication application = (RecipeApplication) getApplicationContext();
        user = application.getUser();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!String.valueOf(phoneText.getText()).equals("")) {
                    user.setPhone(String.valueOf(phoneText.getText()));
                    Intent intent = new Intent(ModifyPhoneActivity.this, ModifyService.class);
                    intent.putExtra("user", JSONObject.toJSONString(user));
                    startService(intent);
                    finish();
                } else {
                    new AlertDialog.Builder(ModifyPhoneActivity.this)
                            .setTitle("修改失败!")
                            .setMessage("手机号不能为空!")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}