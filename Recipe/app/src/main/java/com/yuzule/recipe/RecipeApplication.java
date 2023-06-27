package com.yuzule.recipe;

import android.app.Application;

import com.yuzule.recipe.model.Collections;
import com.yuzule.recipe.model.Recipe;
import com.yuzule.recipe.model.User;

public class RecipeApplication extends Application {

    // 用户信息
    private User user;
    // 收藏夹信息
    private Collections collections;

    public Collections getCollections() {
        return collections;
    }

    public void setCollections(Collections collections) {
        this.collections = collections;
    }

    public void insertCollections(Recipe recipe) {
        collections.getContents().add(recipe);
    }

    public void deleteCollections(Recipe recipe) {
        for (int i = 0; i < collections.getContents().size(); i++) {
            if (recipe.getId() == collections.getContents().get(i).getId())
                collections.getContents().remove(i);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
