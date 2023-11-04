package com.example.testetcc;



import java.sql.*;

public class DAO {
    private Connection conn;

    public DAO() throws SQLException {
        this.conn = DriverManager.getConnection("jdbc:mariadb://10.0.2.2:3306/dados_seguranca_publica", "root", "password");
    }

    public void insert() throws SQLException {
        Statement stmt = conn.createStatement();
    }
}