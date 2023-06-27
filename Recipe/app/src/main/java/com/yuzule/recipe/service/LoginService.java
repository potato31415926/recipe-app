package com.yuzule.recipe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.alibaba.fastjson2.JSON;
import com.yuzule.recipe.api.Api;
import com.yuzule.recipe.api.ApiClient;
import com.yuzule.recipe.model.User;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginService extends Service {


    private static final String TAG = "LoginService";
    Api api;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String phone = intent.getStringExtra("phone");
        String password = intent.getStringExtra("password");
        // 访问接口并获取数据
        login(phone, password);
        return super.onStartCommand(intent, flags, startId);
    }

    private void login(String phone, String password) {
        api = ApiClient.getRetrofit().create(Api.class);
        api.login(phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        // 这里可以添加请求开始时的一些操作，比如显示进度条等
                    }

                    @Override
                    public void onNext(User user) {
                        // 创建Intent对象
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("com.yzl.broadcast.login");
                        broadcastIntent.putExtra("user", JSON.toJSONString(user));
                        // 发送Broadcast
                        sendBroadcast(broadcastIntent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 请求失败时的操作
                        Log.e(TAG, "访问登录接口时出现错误:" + e);
                    }

                    @Override
                    public void onComplete() {
                        // 请求完成时的操作

                    }
                });
    }

}
