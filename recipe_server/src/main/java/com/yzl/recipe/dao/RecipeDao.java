package com.yzl.recipe.dao;

import com.alibaba.fastjson2.JSONObject;
import com.yzl.recipe.model.Collect;
import com.yzl.recipe.model.Recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RecipeDao extends BaseDao {

    public Collect getCollection(int user_id) {
        Collect collect = new Collect();
        ArrayList<Recipe> contents = new ArrayList<>();
        String sql = " SELECT content FROM collect,recipe WHERE collect.content_id = recipe.content_id AND user_id = ? ";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement((sql))) {
            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            // 执行更新操作并检查是否有做更改
            while (resultSet.next()) {
                contents.add(JSONObject.parseObject(resultSet.getString("content"), Recipe.class));
            }
            collect.setContents(contents.toArray(new Recipe[contents.size()]));
            return collect;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String collect(int user_id, int content_id, String content) {
        String sql = " INSERT INTO recipe(content_id,content) VALUES (?,?) "
                + "ON DUPLICATE KEY UPDATE content=VALUES(content)";
        try (Connection conn = dataSource.getConnection()
        ) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, content_id);
            preparedStatement.setString(2, content);
            // 先在recipe表中加入菜谱信息
            preparedStatement.executeUpdate();
            sql = " INSERT INTO collect(user_id,content_id) VALUES(?,?) "
                    + "ON DUPLICATE KEY UPDATE content_id=VALUES(content_id)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, content_id);
            preparedStatement.executeUpdate();
            return "收藏成功";
        } catch (SQLException e) {
            e.printStackTrace();
            return "收藏失败";
        }
    }

    public String unCollect(int user_id, int content_id) {
        String sql = " DELETE FROM collect WHERE user_id = ? AND content_id = ? ";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, content_id);
            // 先在recipe表中加入菜谱信息
            preparedStatement.executeUpdate();
            return "取消收藏成功";
        } catch (SQLException e) {
            e.printStackTrace();
            return "取消收藏失败";
        }
    }

}
