package com.yzl.recipe.controller;

import com.alibaba.fastjson2.JSONObject;
import com.yzl.recipe.dao.RecipeDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/collect")
public class CollectServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        RecipeDao recipeDao = new RecipeDao();
        StringBuffer data = new StringBuffer();
        String line = "";
        String message = "";
        JSONObject json;
        BufferedReader reader;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine()))
                data.append(line);
            json = JSONObject.parseObject(String.valueOf(data));
            message = recipeDao.collect((int) json.get("user_id"), (int) json.get("content_id"), json.getString("content"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject json2 = new JSONObject();
        json2.put("message", message);
        response.getWriter().print(json2);
    }
}
