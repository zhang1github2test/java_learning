package cn.szyrm.spring.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public void insert (){
        String sql = "insert into student (name,age) value (?,?)";
        String username = UUID.randomUUID().toString().substring(0,5);
        jdbcTemplate.update(sql,username,20);
    }
}
