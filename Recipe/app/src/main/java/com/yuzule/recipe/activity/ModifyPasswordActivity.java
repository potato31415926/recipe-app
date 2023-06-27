package com.yuzule.recipe.activity;

import android.content.DialogInterface;
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

public class ModifyPasswordActivity extends AppCompatActivity {


    MaterialToolbar toolbar;
    EditText passwordText, passwordConfirmText;
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
        setContentView(R.layout.activity_modify_password);
        toolbar = findViewById(R.id.toolbar);
        passwordText = findViewById(R.id.passwordText);
        passwordConfirmText = findViewById(R.id.password_confirmText);
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
                if (String.valueOf(passwordText.getText()).equals(String.valueOf(passwordConfirmText.getText()))) {
                    user.setPassword(String.valueOf(passwordText.getText()));
                    Intent intent = new Intent(ModifyPasswordActivity.this, ModifyService.class);
                    intent.putExtra("user", JSONObject.toJSONString(user));
                    startService(intent);
                    finish();
                } else {
                    new AlertDialog.Builder(ModifyPasswordActivity.this)
                            .setTitle("修改失败!")
                            .setMessage("密码输入不一致!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    passwordConfirmText.setText("");
                                }
                            })
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