package util

import java.sql.Connection

class ConexaoBanco {

    static Connection conectar() {
        IConnectionFactory fabrica = PostgresConnectionFactory.getInstancia()
        return fabrica.getConnection()
    }
}