package com.test.service;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.jdbc.core.JdbcTemplate;
import com.minis.jdbc.core.RowMapper;
import com.test.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserService {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
//	public User getUserInfo(int userid) {
//		final String sql = "select id, name,birthday from users where id="+userid;
//		return (User)jdbcTemplate.query(
//				(stmt)->{			
//					ResultSet rs = stmt.executeQuery(sql);
//					User rtnUser = null;
//					if (rs.next()) {
//						rtnUser = new User();
//						rtnUser.setId(userid);
//						rtnUser.setName(rs.getString("name"));
//						rtnUser.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
//					} else {
//					}
//					return rtnUser;
//				}
//		);
//	}
	
	public User getUserInfo(int userid) {
		final String sql = "select id, name,birthday from user where id=?";
		return (User)jdbcTemplate.query(sql, new Object[]{Integer.valueOf(userid)},
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
	
	public List<User> getUsers(int userid) {
		final String sql = "select id, name,birthday from user where id>?";
		return (List<User>)jdbcTemplate.query(sql, new Object[]{Integer.valueOf(userid)},
						new RowMapper<User>(){
							public User mapRow(ResultSet rs, int i) throws SQLException {
								User rtnUser = new User();
								rtnUser.setId(rs.getInt("id"));
								rtnUser.setName(rs.getString("name"));
								rtnUser.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
		
								return rtnUser;
							}
						}
		);
	}
}
