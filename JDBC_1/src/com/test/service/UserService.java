package com.test.service;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.jdbc.core.JdbcTemplate;
import com.test.entity.User;

import java.sql.Date;
import java.sql.ResultSet;

public class UserService {
    @Autowired
    JdbcTemplate jdbcTemplate;
    public User getUserInfo(int userid)
    {
        final String sql="select id,name,birthday from user where id=?";
        return (User) jdbcTemplate.query(sql, new Object[]{Integer.valueOf(userid)},
                (ps) -> {
                    ResultSet re=ps.executeQuery(sql);
                    User user=null;
                    if(re.next()){
                        user=new User();
                        user.setId(re.getInt("id"));
                        user.setName(re.getString("name"));
                        user.setBirthday(new Date(re.getDate("birthday").getTime()));
                    }else {

                    }
                    return user;
                }
        );
    }
}
