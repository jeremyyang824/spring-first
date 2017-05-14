package com.jeremyyang.dao;

import com.jeremyyang.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    private final static String GET_USER_BY_USERNAME_SQL = "SELECT user_id, user_name, credits FROM t_user WHERE user_name=?";
    private final static String MATCH_USER_COUNT_SQL = "SELECT count(*) FROM t_user WHERE user_name =? AND password=? ";
    private final static String UPDATE_LOGIN_INFO_SQL = "UPDATE t_user SET last_visit=?,last_ip=?,credits=?  WHERE user_id =?";

    public int getMatchCount(String userName, String password) {
        return jdbcTemplate.queryForObject(MATCH_USER_COUNT_SQL, new Object[]{userName, password}, Integer.class);
    }

    public User findUserByUserName(final String userName) {
        final User user = new User();
        jdbcTemplate.query(GET_USER_BY_USERNAME_SQL, new Object[]{userName},
                new RowCallbackHandler() {
                    public void processRow(ResultSet rs) throws SQLException {
                        user.setUserId(rs.getInt("user_id"));
                        user.setUserName(userName);
                        user.setCredits(rs.getInt("credits"));
                    }
                });
        return user;
    }

    public void updateLoginInfo(User user) {
        jdbcTemplate.update(UPDATE_LOGIN_INFO_SQL, user.getLastVisit(), user.getLastIp(), user.getCredits(), user.getUserId());
    }


    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
