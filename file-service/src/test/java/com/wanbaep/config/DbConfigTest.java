package com.wanbaep.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DbConfig.class)
public class DbConfigTest {
    @Autowired
    private DataSource dataSource;

    @Test
    public void connectionTest() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

}