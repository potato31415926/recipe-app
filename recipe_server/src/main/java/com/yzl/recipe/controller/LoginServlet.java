package com.yzl.recipe.controller;

import com.alibaba.fastjson2.JSON;
import com.yzl.recipe.dao.UserDao;
import com.yzl.recipe.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        UserDao userDao = new UserDao();
        User user = null;
        try {
            user = userDao.login(phone, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.getWriter().print(JSON.toJSONString(user));
    }
}
