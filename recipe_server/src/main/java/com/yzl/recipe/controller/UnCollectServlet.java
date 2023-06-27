package com.yzl.recipe.controller;

import com.alibaba.fastjson2.JSONObject;
import com.yzl.recipe.dao.RecipeDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/uncollect")
public class UnCollectServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        int user_id = Integer.parseInt(request.getParameter("user_id"));
        int content_id = Integer.parseInt(request.getParameter("content_id"));
        RecipeDao recipeDao = new RecipeDao();
        String message = recipeDao.unCollect(user_id, content_id);
        JSONObject json2 = new JSONObject();
        json2.put("message", message);
        response.getWriter().print(json2);
    }
}
