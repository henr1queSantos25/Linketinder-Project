package dao

import util.ConexaoBanco
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class InteracaoDAO {

    boolean curtirVaga(int candidatoId, int vagaId) {
        Connection conn = ConexaoBanco.conectar()
        if (!conn) {
            return false
        }

        boolean isMatch = false
        try {
            String sqlInsert = "INSERT INTO curtidas_candidatos (candidato_id, vaga_id) VALUES (?, ?) ON CONFLICT DO NOTHING"
            PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)
            stmtInsert.setInt(1, candidatoId)
            stmtInsert.setInt(2, vagaId)
            stmtInsert.executeUpdate()

            String sqlCheck = """
                SELECT e.id AS empresa_id 
                FROM empresas e
                JOIN vagas v ON e.id = v.empresa_id
                JOIN curtidas_empresas ce ON ce.empresa_id = e.id
                WHERE v.id = ? AND ce.candidato_id = ?
            """
            PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)
            stmtCheck.setInt(1, vagaId)
            stmtCheck.setInt(2, candidatoId)
            ResultSet rs = stmtCheck.executeQuery()

            if (rs.next()) {
                int empresaId = rs.getInt("empresa_id")
                isMatch = consolidarMatch(candidatoId, empresaId, vagaId, conn)
            }
        } catch (Exception e) {}
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
            stmtInsert.setInt(1, empresaId)
            stmtInsert.setInt(2, candidatoId)
            stmtInsert.executeUpdate()

            String sqlCheck = """
                SELECT v.id AS vaga_id 
                FROM vagas v
                JOIN curtidas_candidatos cc ON cc.vaga_id = v.id
                WHERE v.empresa_id = ? AND cc.candidato_id = ?
            """
            PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)
            stmtCheck.setInt(1, empresaId)
            stmtCheck.setInt(2, candidatoId)
            ResultSet rs = stmtCheck.executeQuery()

            if (rs.next()) {
                vagaMatchId = rs.getInt("vaga_id")
                consolidarMatch(candidatoId, empresaId, vagaMatchId, conn)
            }
        } catch (Exception e) {}
        finally {
            conn?.close()
        }

        return vagaMatchId
    }

    private boolean consolidarMatch(int candidatoId, int empresaId, int vagaId, Connection conn) {
        try {
            String sqlMatch = "INSERT INTO matches (candidato_id, empresa_id, vaga_id) VALUES (?, ?, ?) ON CONFLICT DO NOTHING"
            PreparedStatement stmtMatch = conn.prepareStatement(sqlMatch)
            stmtMatch.setInt(1, candidatoId)
            stmtMatch.setInt(2, empresaId)
            stmtMatch.setInt(3, vagaId)
            return stmtMatch.executeUpdate() > 0
        } catch (Exception e) {
            return false
        }
    }
}