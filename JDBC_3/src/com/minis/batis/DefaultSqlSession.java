package com.minis.batis;

import javax.sql.DataSource;
import com.minis.jdbc.core.JdbcTemplate;
import com.minis.jdbc.core.PreparedStatementCallback;

public class DefaultSqlSession implements SqlSession{
    JdbcTemplate jdbcTemplate;

    SqlSessionFactory sqlSessionFactory;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
    public SqlSessionFactory getSqlSessionFactory() {
        return this.sqlSessionFactory;
    }
    @Override
    public Object selectOne(String sqlid, Object[] args, PreparedStatementCallback pstmtcallback) {
        String sql = this.sqlSessionFactory.getMapperNode(sqlid).getSql();
        return jdbcTemplate.query(sql, args, pstmtcallback);
    }

    private void buildParameter(){
    }

    private Object resultSet2Obj() {
        return null;
    }
}
