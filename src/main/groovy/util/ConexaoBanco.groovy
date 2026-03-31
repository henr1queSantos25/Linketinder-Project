    package util

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class ConexaoBanco {
    private static final String URL = "URL_DO_BANCO"
    private static final String USUARIO = "USER_DO_BANCO"
    private static final String SENHA = "PASS_DO_BANCO"

    static Connection conectar() {
        try {
            Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA)
            return conexao
        } catch (SQLException e) {
            println("[ERRO] Falha ao ligar à base de dados: ${e.message}")
            return null
        }
    }
}