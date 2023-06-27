package com.yzl.recipe.controller;

import com.alibaba.fastjson2.JSONObject;
import com.yzl.recipe.dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/pictures")
public class GetPicturesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        UserDao userDao = new UserDao();
        JSONObject json = new JSONObject();
        json.put("pictures", userDao.getPictures());
        response.getWriter().print(json);
    }
}
