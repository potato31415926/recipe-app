package com.yuzule.recipe.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson2.JSONObject;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.rey.material.widget.Switch;
import com.yuzule.recipe.R;
import com.yuzule.recipe.RecipeApplication;
import com.yuzule.recipe.activity.HomeActivity;
import com.yuzule.recipe.activity.LoginActivity;
import com.yuzule.recipe.adapter.InformationAdapter;
import com.yuzule.recipe.adapter.PictureListAdapter;
import com.yuzule.recipe.model.Pictures;
import com.yuzule.recipe.model.User;
import com.yuzule.recipe.service.ModifyService;
import com.yuzule.recipe.service.PicturesService;
import com.yuzule.recipe.view.NestedRecyclerView;

import es.dmoral.toasty.Toasty;

public class MineFragment extends Fragment {

    ImageView image;
    TextView user_name;
    TextView edit_user_name;
    MaterialButton logout;
    NestedRecyclerView recyclerView;
    Switch theme_switch;
    User user;
    String[] content = new String[2];
    String[] pictures;
    private final BroadcastReceiver imageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.yzl.broadcast.pictures".equals(intent.getAction())) {
                pictures = JSONObject.parseObject(intent.getStringExtra("pictures"), Pictures.class).getPictures();
            }
        }
    };
    Handler handler = new Handler(Looper.getMainLooper());
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.yzl.broadcast.modify".equals(intent.getAction())) {
                String message = intent.getStringExtra("message");
                Toasty.success(getContext(), message, Toasty.LENGTH_SHORT).show();
                user = JSONObject.parseObject(intent.getStringExtra("user"), User.class);
                content[0] = user.getPassword();
                content[1] = user.getPhone();
                updateUserUI();
            }
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, container, false);
        image = view.findViewById(R.id.image);
        user_name = view.findViewById(R.id.user_name);
        edit_user_name = view.findViewById(R.id.edit_user_name);
        recyclerView = view.findViewById(R.id.information_list);
        logout = view.findViewById(R.id.logout);
        theme_switch = view.findViewById(R.id.theme_button);
        RecipeApplication application = (RecipeApplication) getActivity().getApplicationContext();

        user = application.getUser();

        Intent intent = new Intent(getContext(), PicturesService.class);
        getActivity().startService(intent);

        Glide.with(getContext())
                .load(user.getUrl())
                .into(image);

        user_name.setText(user.getUser_name());

        content[0] = user.getPassword();
        content[1] = user.getPhone();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new InformationAdapter(content));

        SharedPreferences prefs = getActivity().getSharedPreferences("Recipe", MODE_PRIVATE);
        if (prefs.getInt("theme", 0) == 1) {
            theme_switch.setChecked(true);
        }

        image.setOnClickListener(new View.OnClickListener() {

            final LayoutInflater inflater = LayoutInflater.from(getActivity());
            final View customView = inflater.inflate(R.layout.picture_dialog, null);
            final NestedRecyclerView nestedRecyclerView = customView.findViewById(R.id.pictures);

            @Override
            public void onClick(View v) {
                // 获取 customView 的父容器
                ViewGroup parentView = (ViewGroup) customView.getParent();
                if (parentView != null) {
                    // 将 customView 从原来的父容器中移除
                    parentView.removeView(customView);
                }
                nestedRecyclerView.setAdapter(new PictureListAdapter(user, pictures));
                nestedRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                new AlertDialog.Builder(getActivity())
                        .setTitle("修改头像")
                        .setView(customView)
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });

        edit_user_name.setOnClickListener(new View.OnClickListener() {
            final LayoutInflater inflater = LayoutInflater.from(getActivity());
            final View customView = inflater.inflate(R.layout.nickname_dialog, null);
            final EditText input = customView.findViewById(R.id.editText);


            @Override
            public void onClick(View v) {
                // 获取 customView 的父容器
                ViewGroup parentView = (ViewGroup) customView.getParent();
                if (parentView != null) {
                    // 将 customView 从原来的父容器中移除
                    parentView.removeView(customView);
                }
                new AlertDialog.Builder(getActivity())
                        .setTitle("修改昵称")
                        .setView(customView)
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                input.setText("");
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                user.setUser_name(String.valueOf(input.getText()));
                                application.setUser(user);
                                input.setText("");
                                Intent intent = new Intent(getActivity(), ModifyService.class);
                                intent.putExtra("user", JSONObject.toJSONString(user));
                                getContext().startService(intent);
                            }
                        })
                        .show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setMessage("你确定要退出当前登录吗?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getContext().startActivity(new Intent(getActivity(), LoginActivity.class));
                            }
                        })
                        .show();
            }
        });

        theme_switch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("您确定要切换主题吗?")
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (checked) {
                                    prefs.edit().putInt("theme", 1).commit();
                                } else {
                                    prefs.edit().putInt("theme", 0).commit();
                                }
                                Intent intent1 = new Intent(getActivity(), HomeActivity.class);
                                intent1.putExtra("mine", "mine");
                                startActivity(intent1);
                            }
                        })
                        .show();
            }
        });

        return view;
    }

    private void updateUserUI() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Glide.with(getContext())
                        .load(user.getUrl())
                        .into(image);
                user_name.setText(user.getUser_name());
                // 传入的user有变化，重新设置一遍
                recyclerView.setAdapter(new InformationAdapter(content));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.yzl.broadcast.modify");
        filter.addAction("com.yzl.broadcast.pictures");
        getActivity().registerReceiver(receiver, filter);
        getActivity().registerReceiver(imageReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
        getActivity().unregisterReceiver(imageReceiver);

    }
}