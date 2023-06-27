package com.yuzule.recipe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.alibaba.fastjson2.JSONObject;
import com.yuzule.recipe.RecipeApplication;
import com.yuzule.recipe.api.Api;
import com.yuzule.recipe.api.ApiClient;
import com.yuzule.recipe.model.Collections;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CollectionsService extends Service {


    private static final String TAG = "CollectionsService";
    Api api;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int user_id = ((RecipeApplication) getApplicationContext()).getUser().getUser_id();
        // 访问接口并获取数据
        getCollections(user_id);
        return super.onStartCommand(intent, flags, startId);
    }

    private void getCollections(int user_id) {
        api = ApiClient.getRetrofit().create(Api.class);
        api.getCollections(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Collections>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        // 这里可以添加请求开始时的一些操作，比如显示进度条等
                    }

                    @Override
                    public void onNext(Collections collections) {
                        // 创建Intent对象
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("com.yzl.broadcast.collections");
                        broadcastIntent.putExtra("collections", JSONObject.toJSONString(collections));
                        // 发送Broadcast
                        sendBroadcast(broadcastIntent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 请求失败时的操作
                        Log.e(TAG, "访问获取收藏数据接口时出现错误:" + e);
                    }

                    @Override
                    public void onComplete() {
                        // 请求完成时的操作

                    }
                });
    }

}
