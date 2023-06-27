package com.yzl.recipe.controller;

import com.alibaba.fastjson2.JSONObject;
import com.yzl.recipe.dao.UserDao;
import com.yzl.recipe.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/modify")
public class ModifyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        StringBuffer data = new StringBuffer();
        String line = "";
        BufferedReader reader = null;
        User user = null;
        UserDao userDao = new UserDao();
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine()))
                data.append(line);
            user = JSONObject.parseObject(String.valueOf(data), User.class);
            line = userDao.modify(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        json.put("message", line);
        response.getWriter().print(json);
    }
}
