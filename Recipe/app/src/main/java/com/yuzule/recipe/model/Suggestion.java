package com.yuzule.recipe.model;

import android.annotation.SuppressLint;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

@SuppressLint("ParcelCreator")
public class Suggestion implements SearchSuggestion {
    private final String mTitle;

    public Suggestion(String title) {
        mTitle = title;
    }

    @Override
    public String getBody() {
        return mTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

    }
}