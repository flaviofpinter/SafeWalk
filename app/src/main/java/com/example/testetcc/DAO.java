package com.example.testetcc;


import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DAO {
    private final Connection conn;

    public DAO() throws SQLException {
        this.conn = DriverManager.getConnection("jdbc:mariadb://10.0.2.2:3306/dados_seguranca_publica", "root", "password");
    }

    public void inserirOcorrencia(String nomeRua, String bairro, boolean policiamento, boolean iluminacao, boolean comercio, boolean movimentacao) throws SQLException {
        Statement stmt = conn.createStatement();
        int idOcorrencia = getLastId(stmt, "index_ocorrencia");
        int idRua = inserirRua(stmt, nomeRua, bairro);
        String sql = "INSERT INTO index_ocorrencia VALUES(" + idOcorrencia + ", " +
                idRua + ", " +
                (policiamento ? "'sim'" : "'não'") + ", " +
                (iluminacao ? "'sim'" : "'não'") + ", " +
                (comercio ? "'sim'" : "'não'") + ", " +
                (movimentacao ? "'sim'" : "'não'") + ")";
        stmt.executeQuery(sql);
    }

    private int inserirRua(Statement ruaStmt, String nomeRua, String bairro) throws SQLException {
        int idAtual = getLastId(ruaStmt, "endereco");
        System.out.println(idAtual);
        String ruaSql = "INSERT INTO endereco VALUES(" + idAtual + ", '" + nomeRua + "', '" + bairro + "')" + "RETURNING id";
        ruaStmt.executeQuery(ruaSql);
        return idAtual;
    }

    private int getLastId(Statement statement, String tabela) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT id FROM " + tabela + " ORDER BY id DESC LIMIT 1");
        return (rs.next() ? rs.getInt(1) : 0) + 1;
    }
    public List<String> getRuas() throws SQLException {
        List<String> result = new LinkedList<String>();
        Statement stmt = conn.createStatement();
        String sql = "SELECT nome_da_rua FROM endereco LIMIT 20";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()){
            result.add(rs.getString(1));
        }
        return result;
    }
}