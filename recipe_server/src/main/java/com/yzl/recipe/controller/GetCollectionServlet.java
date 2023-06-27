package com.yzl.recipe.controller;

import com.alibaba.fastjson2.JSONObject;
import com.yzl.recipe.dao.RecipeDao;
import com.yzl.recipe.model.Collect;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/collection")
public class GetCollectionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        int user_id = Integer.parseInt(request.getParameter("user_id"));
        RecipeDao recipeDao = new RecipeDao();
        Collect collections = recipeDao.getCollection(user_id);
        response.getWriter().print(JSONObject.toJSONString(collections));
    }
}
