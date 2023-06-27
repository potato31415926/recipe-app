package com.yzl.recipe.controller;

import com.alibaba.fastjson2.JSONObject;
import com.yzl.recipe.dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String user_name = request.getParameter("user_name");
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("passwordConfirm");
        String phone = request.getParameter("phone");
        UserDao userDao = new UserDao();
        String message = null;
        // 两次密码输入不一致
        if (!password.equals(passwordConfirm)) {
            message = "密码输入不一致";
        } else {
            try {
                message = userDao.register(user_name, password, phone);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        JSONObject json = new JSONObject();
        json.put("message", message);
        response.getWriter().print(json);

    }
}
