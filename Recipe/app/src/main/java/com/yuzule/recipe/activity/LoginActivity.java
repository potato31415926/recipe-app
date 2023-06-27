package com.yuzule.recipe.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson2.JSONObject;
import com.google.android.material.button.MaterialButton;
import com.yuzule.recipe.DBHelper;
import com.yuzule.recipe.R;
import com.yuzule.recipe.RecipeApplication;
import com.yuzule.recipe.model.User;
import com.yuzule.recipe.service.LoginService;

public class LoginActivity extends AppCompatActivity {

    SQLiteDatabase db;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.yzl.broadcast.login".equals(intent.getAction())) {
                String data = intent.getStringExtra("user");
                User user = JSONObject.parseObject(data, User.class);
                // 从后端获取的用户电话号码为空
                if (user.getPhone().isEmpty()) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("登录失败!")
                            .setMessage("手机号或密码错误!")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                } else {
                    db.execSQL("INSERT OR IGNORE INTO users VALUES(" + user.getPhone() + "," + user.getPassword() + ")");
                    Intent intent1 = new Intent(LoginActivity.this, HomeActivity.class);
                    ((RecipeApplication) getApplicationContext()).setUser(user);
                    startActivity(intent1);
                }
            }
        }
    };
    private MaterialButton registerButton, loginButton;
    private EditText phoneEditText, passwordEditText;

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
        setContentView(R.layout.activity_login);

        // 获取控件
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
        phoneEditText = findViewById(R.id.phoneText);
        passwordEditText = findViewById(R.id.passwordText);

        passwordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // 注册广播接收器
        IntentFilter filter = new IntentFilter("com.yzl.broadcast.login");
        registerReceiver(receiver, filter);

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT DISTINCT * FROM users", null);
        while (c.moveToNext()) {
            phoneEditText.setText(c.getString(0));
            passwordEditText.setText(c.getString(1));
        }
        c.close();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        // 设置事件监听器
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, LoginService.class);
                intent.putExtra("phone", phoneEditText.getText().toString());
                intent.putExtra("password", passwordEditText.getText().toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startService(intent);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消注册广播接收器
        unregisterReceiver(receiver);
    }
}