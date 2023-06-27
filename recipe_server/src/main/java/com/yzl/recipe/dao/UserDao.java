package com.yzl.recipe.dao;

import com.yzl.recipe.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDao extends BaseDao {

    public User login(String phone, String password) throws SQLException {
        User user = new User();
        user.setUser_id(0);
        user.setPhone("");
        user.setPassword("");
        user.setUser_name("");
        String sql = " SELECT user_id,user_name,password,phone,url FROM user,picture WHERE phone = ? && user.picture_id = picture.picture_id ";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement((sql))) {
            preparedStatement.setString(1, phone);
            ResultSet resultSet = preparedStatement.executeQuery();
            // 查找到电话对应的用户并且密码输入正确
            if (resultSet.next() && resultSet.getString("password").equals(password)) {
                user.setUser_id(resultSet.getInt("user_id"));
                user.setUser_name(resultSet.getString("user_name"));
                user.setPassword(resultSet.getString("password"));
                user.setPhone((resultSet.getString("phone")));
                user.setUrl((resultSet.getString("url")));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String register(String user_name, String password, String phone) throws SQLException {
        String sql = " SELECT COUNT(*) AS count FROM user WHERE user_name = ? ";
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement((sql));
            preparedStatement.setString(1, user_name);
            ResultSet resultSet = preparedStatement.executeQuery();
            // 若该用户名已存在
            if (resultSet.next() && resultSet.getInt("count") != 0) {
                return "该用户名已存在";

            } else {
                sql = " SELECT COUNT(*) AS count FROM user WHERE phone = ? ";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, phone);
                resultSet = preparedStatement.executeQuery();
                // 手机号已存在
                if (resultSet.next() && resultSet.getInt("count") != 0) {
                    return "该手机号已注册";
                } else {
                    sql = " INSERT INTO user (user_name,password,phone) VALUES (?,?,?) ";
                    preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1, user_name);
                    preparedStatement.setString(2, password);
                    preparedStatement.setString(3, phone);
                    if (preparedStatement.executeUpdate() == 1) {
                        return "注册成功";
                    } else {
                        return "注册失败";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "数据库异常!";
        }
    }

    public String modify(User user) {
        String sql = " UPDATE user SET user_name = ?,password = ?,phone = ?,picture_id = (SELECT picture_id FROM picture WHERE url = ?) WHERE user_id = ? ";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement((sql))) {
            preparedStatement.setString(1, user.getUser_name());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getUrl());
            preparedStatement.setInt(5, user.getUser_id());
            // 执行更新操作并检查是否有做更改
            if (preparedStatement.executeUpdate() != 0) {
                return "修改完成";
            }
            return "未作修改";
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] getPictures() {
        ArrayList<String> pictures = new ArrayList<String>();
        String sql = " SELECT DISTINCT url FROM picture ";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement((sql))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            // 执行更新操作并检查是否有做更改
            while (resultSet.next()) {
                pictures.add(resultSet.getString("url"));
            }
            return pictures.toArray(new String[pictures.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}
