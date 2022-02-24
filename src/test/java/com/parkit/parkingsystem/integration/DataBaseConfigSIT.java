package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.config.DataBaseConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class DataBaseConfigSIT {
    DataBaseConfig dataBaseConfig = new DataBaseConfig();
    private Connection con;
    private ResultSet rs;
    private PreparedStatement ps;

    @BeforeEach
    public void setUp() throws ClassNotFoundException, SQLException, IOException {

        dataBaseConfig = new DataBaseConfig();

        Properties properties = new Properties();
        properties.load(new FileInputStream("src/test/resources/connection.properties"));
        Class.forName(properties.getProperty("driver_class_name"));
        String url = properties.getProperty("urlTest");
        String root = properties.getProperty("user");
        String password = properties.getProperty("password");
        con = DriverManager.getConnection(url,root,password);

        ps = con.prepareStatement("select * from parking");
    }

    @Test
    void getConnectionNotNull() throws ClassNotFoundException, SQLException, IOException {

        assertNotNull(dataBaseConfig.getConnection());
    }

    @Test
    void closeConnection() throws SQLException {

        // When
        dataBaseConfig.closeConnection(con);
        // then
        assertTrue(ps.isClosed());

    }
    @Test
    void closePreparedStatementClosePreparedStatement()
            throws SQLException {
        // When
        dataBaseConfig.closePreparedStatement(ps);
        // Then
        assertTrue(ps.isClosed());
    }
    @Test
    void closeResultCloseResultSet() throws SQLException {
        // Given
        rs = ps.executeQuery();
        // When
        dataBaseConfig.closeResultSet(rs);
        // Then
        assertTrue(rs.isClosed());
    }
}
