package com.test.service;

import com.minis.batis.SqlSession;
import com.minis.batis.SqlSessionFactory;
import com.minis.beans.factory.annotation.Autowired;
import com.test.entity.User;

import java.sql.ResultSet;

public class UserService {
	@Autowired
	SqlSessionFactory sqlSessionFactory;

	public User getUserInfo(int userid) {
		//final String sql = "select id, name,birthday from users where id=?";
		String sqlid = "com.test.entity.User.getUserInfo";
		SqlSession sqlSession = sqlSessionFactory.openSession();
		return (User)sqlSession.selectOne(sqlid, new Object[]{Integer.valueOf(userid)},
				(pstmt)->{
					ResultSet rs = pstmt.executeQuery();
					User rtnUser = null;
					if (rs.next()) {
						rtnUser = new User();
						rtnUser.setId(userid);
						rtnUser.setName(rs.getString("name"));
						rtnUser.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
					} else {
					}
					return rtnUser;
				}
		);
	}
}
