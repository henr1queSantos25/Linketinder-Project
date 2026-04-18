package dao

import util.ConexaoBanco
import util.JdbcBinder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.logging.Level
import java.util.logging.Logger

class InteracaoDAO {

    private static final Logger LOGGER = Logger.getLogger(InteracaoDAO.name)

    boolean curtirVaga(int candidatoId, int vagaId) {
        Connection conn = ConexaoBanco.conectar()
        if (!conn) {
            return false
        }

        boolean isMatch = false
        try {
            String sqlInsert = "INSERT INTO curtidas_candidatos (candidato_id, vaga_id) VALUES (?, ?) ON CONFLICT DO NOTHING"
            PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)
            JdbcBinder.bind(stmtInsert, candidatoId, vagaId)
            stmtInsert.executeUpdate()

            String sqlCheck = """
                SELECT e.id AS empresa_id 
                FROM empresas e
                JOIN vagas v ON e.id = v.empresa_id
                JOIN curtidas_empresas ce ON ce.empresa_id = e.id
                WHERE v.id = ? AND ce.candidato_id = ?
            """
            PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)
            JdbcBinder.bind(stmtCheck, vagaId, candidatoId)
            ResultSet rs = stmtCheck.executeQuery()

            if (rs.next()) {
                int empresaId = rs.getInt("empresa_id")
                isMatch = consolidarMatch(candidatoId, empresaId, vagaId, conn)
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Falha ao curtir vaga. candidatoId=${candidatoId}, vagaId=${vagaId}", e)
        }
        finally {
            conn?.close()
        }
        return isMatch
    }

    Integer curtirCandidato(int empresaId, int candidatoId) {
        Connection conn = ConexaoBanco.conectar()
        if (!conn) {
            return null
        }

        Integer vagaMatchId = null
        try {
            String sqlInsert = "INSERT INTO curtidas_empresas (empresa_id, candidato_id) VALUES (?, ?) ON CONFLICT DO NOTHING"
            PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)
            JdbcBinder.bind(stmtInsert, empresaId, candidatoId)
            stmtInsert.executeUpdate()

            String sqlCheck = """
                SELECT v.id AS vaga_id 
                FROM vagas v
                JOIN curtidas_candidatos cc ON cc.vaga_id = v.id
                WHERE v.empresa_id = ? AND cc.candidato_id = ?
            """
            PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)
            JdbcBinder.bind(stmtCheck, empresaId, candidatoId)
            ResultSet rs = stmtCheck.executeQuery()

            if (rs.next()) {
                vagaMatchId = rs.getInt("vaga_id")
                consolidarMatch(candidatoId, empresaId, vagaMatchId, conn)
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Falha ao curtir candidato. empresaId=${empresaId}, candidatoId=${candidatoId}", e)
        }
        finally {
            conn?.close()
        }

        return vagaMatchId
    }

    private boolean consolidarMatch(int candidatoId, int empresaId, int vagaId, Connection conn) {
        try {
            String sqlMatch = "INSERT INTO matches (candidato_id, empresa_id, vaga_id) VALUES (?, ?, ?) ON CONFLICT DO NOTHING"
            PreparedStatement stmtMatch = conn.prepareStatement(sqlMatch)
            JdbcBinder.bind(stmtMatch, candidatoId, empresaId, vagaId)
            return stmtMatch.executeUpdate() > 0
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Falha ao consolidar match. candidatoId=${candidatoId}, empresaId=${empresaId}, vagaId=${vagaId}", e)
            return false
        }
    }
}