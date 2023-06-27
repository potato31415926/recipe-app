package com.yuzule.recipe.model;

import java.util.List;

public class Result {
    private int total;
    private int num;
    private List<Recipe> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<Recipe> getList() {
        return list;
    }

    public void setList(List<Recipe> list) {
        this.list = list;
    }
}
