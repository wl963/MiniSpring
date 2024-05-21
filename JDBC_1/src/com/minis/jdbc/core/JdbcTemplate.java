package com.minis.jdbc.core;



import javax.sql.DataSource;
import java.sql.*;
import java.util.Date;

public  class JdbcTemplate {

    private DataSource dataSource;


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }
    public JdbcTemplate() {
    }
    public Object query(StatementCallback statementCallback) {
        Connection con = null;
        Statement stmt = null;
        try {
            con= dataSource.getConnection();

            stmt = con.createStatement();

            return statementCallback.doInStatement(stmt);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                stmt.close();
                con.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public Object query(String sql,Object[] args,PreparedStatementCallback preparedStatementCallback)
    {
        Connection con=null;
        PreparedStatement ps=null;
        try {
            con= dataSource.getConnection();
            ps= con.prepareStatement(sql);
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof String) {
                        ps.setString(i + 1, (String) arg);
                    } else if (arg instanceof Integer) {
                        ps.setInt(i + 1, (int) arg);
                    } else if (arg instanceof Date) {
                        ps.setDate(i + 1, (java.sql.Date) arg);
                    }
                }
            }

            return preparedStatementCallback.doInPreparedStatement(ps);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                ps.close();
                con.close();
            } catch (Exception e) {

            }
        }
    }


}

