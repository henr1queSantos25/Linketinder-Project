package util

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class PostgresConnectionFactory implements IConnectionFactory {

    private static PostgresConnectionFactory instancia
    private Connection conexaoUnica

    private PostgresConnectionFactory() {}

    static PostgresConnectionFactory getInstancia() {
        if (instancia == null) {
            instancia = new PostgresConnectionFactory()
        }
        return instancia
    }

    @Override
    Connection getConnection() {
        try {
            if (conexaoUnica == null || conexaoUnica.isClosed()) {
                String url = "URL_DO_BANCO"
                String usuario = "USER_DO_BANCO"
                String senha = "PASS_DO_BANCO"
                conexaoUnica = DriverManager.getConnection(url, usuario, senha)
            }
            return conexaoUnica
        } catch (SQLException e) {
            return null
        }
    }
}